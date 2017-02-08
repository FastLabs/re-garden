(ns task.core
  (:require [task.spec :as task]))


(defn- task-id [task-def]
  (str "t-" (::task/id task-def)))


(defmulti new-task (fn [{:keys [::task/task-type]} attrs] task-type) :default :simple)



(defmethod new-task :simple [task-def attrs]
  (let [task-id (task-id task-def)]
    {::task/id          task-id
     ::task/task-def    task-def
     ::task/task-status :pending}))

