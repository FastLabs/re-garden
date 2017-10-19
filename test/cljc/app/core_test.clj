(ns app.core-test
  (:require [app.core :as app]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))

(deftest app-meta-test
  (testing "mandatory application fields"
    (is (s/valid? ::app/meta (app/new-app "ap1" "hello world")))))
