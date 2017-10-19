(ns re-garden.handlers
  (:require [re-frame.core :as re-frame]
            [re-garden.db :as db]
            [session.core :as session]))

(re-frame/register-handler
  :initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/register-handler
  :set-applications
  (fn [db [_ applications]]
    (prn "Update applications" applications)
    (assoc db :applications applications)))

(re-frame/register-handler
  :start-app
  (fn [{:keys [sessions] :as db} [_ app]]
    (prn "Start application: " app)
    (let [new-session (session/new-app-session app)
          s (conj sessions new-session)]
      (assoc db :sessions s))))



(re-frame/register-handler
  :set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))
