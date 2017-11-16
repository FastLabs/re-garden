(ns session.core
  (:require [app.core :as app]
            [clojure.spec.alpha :as s]))

(s/def ::active? boolean?)
(s/def ::id (s/cat :session-id int? :app-id ::app/id))
(s/def ::instance (s/keys :req [::id]
                          :opt [::active?]))


(s/def ::all-inst (s/coll-of ::instance))
(s/def ::ins-count int?)
(s/def ::inst-map (s/keys
                    :req [::inst-count]
                    :opt [::all-inst]))



(defn new-session
  [inst-nr {:keys [::app/id]}]
  {::id      [inst-nr id]
   ::active? true})


(defn scan-sessions
  "Finds all sessions for a particular application"
  [sessions {:keys [::app/id]}]
  (filter #(= (second (::id %)) id) sessions))

(defn- gen-id
  [{:keys [::inst-count]} app-meta]
  (inc inst-count))


(defn start-session
  "Appends a new created session "
  ([sessions app-meta]
   (start-session sessions app-meta gen-id))
  ([{:keys [::inst-count ::all-inst]} app-meta s-id-gen]
   (let [new-count (inc inst-count)
         new-session (new-session new-count app-meta)
         {:keys [::id]} new-session]
     {::inst-count new-count
      ::all-inst   (conj all-inst new-session)})))

(defn disable-session
  ([{:keys [::all-inst] :as sessions}]
   (disable-session sessions (first all-inst)))
  ([{:keys [::all-inst] :as sessions} session]
   (let [new-inst []]
     (assoc sessions ::all-inst new-inst))))


(defn activate-tab [tab]
  (assoc tab ::active? true))

(defn- find-first-indexed
  "Find first occurrence of the tab and return the tuple [tab-index tab]"
  [predicate all-tabs]
  (->> all-tabs
       (map-indexed vector)
       (filter #(predicate (second %)))
       first))

(defn- guess-index
  "Find the next active tab, if present this is always the right tab, otherwise is the first from left"
  [all-tabs closed-index]
  (let [tab-count (count all-tabs)]
    (cond-> closed-index
            (= (- tab-count closed-index) 1) dec)))

(defn close-tab
  "Closes the tab that matches the predicate "
  [close-pred all-tabs]
  (let [[closed-index closed-tab] (find-first-indexed close-pred all-tabs)
        active-index (guess-index all-tabs closed-index)]
    (->> all-tabs
         (filter #(not (close-pred %)))
         (map-indexed (fn [tab-index tab]
                        (cond-> tab (and (::active? closed-tab)
                                         (= active-index tab-index)) activate-tab))))))

(defn close-by-id
  [all-tabs tab-id]
  (let [predicate #(= (::id %) tab-id)]
    (close-tab predicate all-tabs)))
