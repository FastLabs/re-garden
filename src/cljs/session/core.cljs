(ns session.core)



(defn new-app-session [{:keys [id] :as app}]
  {:session-id (str id "-session")
   :app-id     id})

