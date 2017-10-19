(ns task.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec :as s]
            [task.spec :as t]
            [task.core :as tasks]))

(defn- has-name [req-name]
  (fn [attrs]
    (let [{:keys [name]} attrs]
      (= name req-name))))

(defn- has-surname [req-name]
  (fn [{:keys [surname]}] (= req-name surname)))


;;Validates a simple task definition

(deftest test-task-def
  (let [t-def {::t/name        "Task 1"
               ::t/description "Task with a single dependency"
               ::t/dep-def     [[:dep-1 (has-name "simple")]]}]
    (is (s/valid? ::t/task-def t-def))))


(deftest test-eval-dep
  (let [t-def {::t/name        "Task 2"
               ::t/description "Task wit 2 dependencies"
               ::t/dep-def     [[:dep-1 (has-name "name")]
                                [:dep-2 (has-surname "surname")]]}]
    (is (s/valid? ::t/task-def t-def))))

(deftest test-task-inst
  (let [t-def {::t/name "Task3"}
        task (tasks/new-task t-def {})]
    (is (s/valid? ::t/task-inst task))
    (is (= {::t/id          "Task3-"
            ::t/task-status :pending
            ::t/task-def    t-def} task))))

(deftest test-task-inst-dependency
  (let [t-def {::t/name    "Task 4"
               ::t/dep-def [[:dep-1 (has-name "oleg")]]}
        task (tasks/new-task t-def {:name "oleg"})]
    (is (= t-def (::t/task-def task)))
    (is (= [[:dep-1 true] (::t/deps task)]))))

(deftest test-task-inst-has-sub
  (let [t1-def {::t/name    "Task 1"
                ::t/dep-def [[:dep-1 (has-name "oleg")]]}
        t0-def {::t/name     "Task 0"
                ::t/sub-defs [t1-def]}
        t0-inst (tasks/new-task t0-def {:name "oleg"})]
    (is (= t0-def (::t/task-def t0-inst)))
    (is (= 1 (count (::t/sub-tasks t0-inst))))
    (let [[sub1] (::t/sub-tasks t0-inst)]
      (is (= t1-def (::t/task-def sub1)))
      (is (tasks/all-deps-meet? (::t/deps sub1))))))

