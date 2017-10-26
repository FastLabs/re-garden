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
                    :opt [ ::all-inst]))



(defn new-session
  [inst-nr {:keys [::app/id]}]
  {::id      [inst-nr id]
   ::active? true})

(defn disable-session [session]
  (dissoc session ::active?))

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

(defn deactivate
  [active-now triplet]
  (filter #(not (= active-now %)) triplet))

(defn close-session
  [session-id sessions]
  (let [[first [closed & rest]] (->> sessions
                                     (map #(cond-> %
                                                   (= session-id (::id %)) (assoc :closed? true)))
                                     (split-with #(not (:closed? %))))]
    (concat first rest)))


(defn find-next-active
  [all-inst]
  (loop [[current next & inst] all-inst result []]
    (if (::active? current)
        (-> (conj result (assoc next ::active true))
            (concat inst))
      (recur (rest inst) result))))




(defn disable-session
  ([{:keys [::all-inst] :as sessions}]
   (disable-session sessions (first all-inst)))
  ([{:keys [::all-inst] :as sessions} session]
   (let [new-inst []]
     (assoc sessions ::all-inst new-inst))))



(defn activate-session [sessions session-id])


