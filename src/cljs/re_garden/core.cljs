(ns re-garden.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [re-garden.handlers]
              [re-garden.subs]
              [re-garden.routes :as routes]
              [re-garden.views :as views]
              [re-garden.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
