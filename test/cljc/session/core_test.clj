(ns session.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [app.core :as app]
            [session.core :as session]))

(deftest session-actions
  (let [app-meta (app/new-app "app-1" "hello world")]
    (testing "create new session"
      (let [app-session (session/new-session 1 app-meta)]
        (is (s/valid? ::session/instance app-session))))

    (testing "find app sessions"
      (let [sessions [(session/new-session 1 app-meta)
                      (session/new-session 2 (app/new-app "app-2" "good buy"))]
            found (session/scan-sessions sessions app-meta)]
        (is (= 1 (count found)))
        (is (= [1 "app-1"] (::session/id (first found))))))

    (testing "start first session"
      (let [s-map {::session/inst-count 0
                   ::session/all-inst   []
                   ::session/all-active []}]
        (is (s/valid? ::session/inst-map (session/start-session s-map app-meta)))))

    (testing "start second session when first active"
      (let [{:keys [::session/id] :as session} (session/new-session 1 app-meta)
            s-map {::session/inst-count 1
                   ::session/all-inst   [session]
                   ::session/all-active [id]}]
        (is (s/valid? ::session/inst-map s-map))
        (let [{:keys [::session/all-inst
                      ::session/all-active]} (session/start-session s-map app-meta)]
          (is (= 2 (count (session/scan-sessions all-inst app-meta))))
          (is (= [[2 "app-1"]] all-active)))))

    (testing "disable current active session"
      (let [disabled-session (->> app-meta
                                  (session/new-session 2)
                                  session/disable-session)]
        (is (s/valid? ::session/instance disabled-session)
            (is (not (::session/active? disabled-session))))))))


(deftest close-sessions
  (testing "simple close session"
    (let [sessions [{::session/id 1}
                    {::session/id 2 ::session/active true}
                    {::session/id 3}]]
      (prn (session/close-session 2 sessions)))))



