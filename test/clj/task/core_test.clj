(ns task.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec :as s]
            [task.spec :as task]
            [task.core :as tasks]))

(deftest test-task-def
  (let [t-def {::task/task-type :simple ::task/description "simple description"}]
       (is (s/valid? ::task/task-def t-def))))

(deftest test-task-inst
  (let [task (tasks/new-task {::task/task-type :simple} {})]
    (is (s/valid? ::task/task-inst task))))

