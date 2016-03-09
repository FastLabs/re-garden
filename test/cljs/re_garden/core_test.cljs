(ns re-garden.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [re-garden.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
