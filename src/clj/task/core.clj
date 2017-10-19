(ns task.core
  (:require [task.spec :as t]
            [clojure.string :as str]))


(defn- task-id [{:keys [::t/name ::t/key-attrs] :as task-def} attrs]
  (let [attr-val (map #(get attrs %) key-attrs)]
    (str name "-" (str/join "-" attr-val))))

(defn- default-task
  "Creates default task based on a specification"
  ([task-def attrs]
   (default-task task-def attrs :pending))
  ([task-def attrs task-status]
   {::t/task-def    task-def
    ::t/id          (task-id task-def attrs)
    ::t/task-status task-status}))

(defn- eval-deps
  "Evaluate the dependencies against the attribute map."
  [{:keys [::t/dep-def]} attrs]
  (->> dep-def
       (map (fn [[name pred]] [name (pred attrs)]))))

(declare temp-task)

(defn- sub-tasks
  "Evaluate sub definitions "
  [{:keys [::t/sub-defs]} attrs]
  (apply concat (map #(temp-task % attrs) sub-defs)))

(defn all-deps-meet?
  "Checks if all dependencies are met"
  [deps]
  (reduce (fn [prev [_ curr]] (and prev curr)) true deps))

(defn any-deps-meet?
  "Checks if any dependency meet: input [[:dep-1 true] [:dep-2 false] ]"
  [deps]
  (reduce (fn [prev [_ curr]] (or prev curr)) false deps))

(defn- temp-task [task-def attrs]
  (let [deps (eval-deps task-def attrs)
        subs (sub-tasks task-def attrs)]
    (if (or (seq subs) (any-deps-meet? deps))
      [(-> task-def
           (default-task attrs)
           (assoc ::t/sub-tasks subs)
           (assoc ::t/deps deps))]
      [])))

(defn new-task
  "Creates a task by filling all the mandatory attributes, evaluates the sub-tasks and fills the  dependencies. If none meet a default task is returned"
  [task-def attrs]
  (let [[t] (temp-task task-def attrs)]
    (if t
      t
      (default-task task-def attrs))))
