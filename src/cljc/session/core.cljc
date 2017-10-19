(ns session.core
  (:require [app.core :as app]
            [clojure.spec.alpha :as s]))

(s/def ::active? boolean?)
(s/def ::id (s/cat :session-id int? :app-id ::app/id))
(s/def ::instance (s/keys :req [::id]
                          :opt [::active?]))


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

(defn- gen-id [sessions app-meta]
  (let [last-session (last (scan-sessions sessions app-meta))
        [last-id] (:id last-session)]
    (if last-id (inc last-id) 0)))


(defn start-session
  "Appends a new created session "
  ([sessions app-meta]
   (start-session sessions app-meta gen-id))
  ([sessions app-meta s-id-gen]
   (let [new-id (apply s-id-gen sessions app-meta)
         new-session (new-session new-id app-meta)]
     (-> (map disable-session sessions)
         (conj new-session)))))

(defn close-session [sessions session-id])
(defn activate-session [sessions session-id])


