(ns session.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [app.core :as app]
            [session.core :as session]))

(deftest session-actions
  (let [app-meta (app/new-app "app-1" "hello world")]
    (testing "create new session"
      (let [app-session (session/new-session 1 app-meta)]
        (is (s/valid? ::session/instance app-session))
        (is (::session/active? app-session))))

    (testing "disable session"
      (let [disabled-session (->> app-meta
                                  (session/new-session 2)
                                  session/disable-session)]
        (is (s/valid? ::session/instance disabled-session))
        (is (not (::session/active? disabled-session)))))

    (testing "find app sessions"
      (let [sessions [(session/new-session 1 app-meta)
                      (session/new-session 2 (app/new-app "app-2" "good buy"))]
            found (session/scan-sessions sessions app-meta)]
        (is (= 1 (count found)))
        (is (= [1 "app-1"] (::session/id (first found))))))))
