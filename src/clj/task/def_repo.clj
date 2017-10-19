(ns task.def-repo
  (:require [task.core :as tasks]))



(defn- scan-task-def [t-defs attrs])

(defprotocol TaskDefRepo
  (save-defs [this defs] "Save the task")
  (match-defs [this attrs]))

(defrecord InMemTaskDefRepo [atom-repo]
  TaskDefRepo
  (save-defs [this defs] (prn "save the specs"))
  (match-defs [this attrs] (prn "match the specs")))


(defn new-task-def-repo []
  (let [atom-repo (atom [])]
    (map->InMemTaskDefRepo {:atom-repo atom-repo})))