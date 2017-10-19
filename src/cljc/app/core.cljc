(ns app.core
  (:require [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::description string?)

(s/def ::meta (s/keys :req [::id ::name]
                      :opt [::description]))


(defn new-app [app-id app-name]
  {::id   app-id
   ::name app-name})
