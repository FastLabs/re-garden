(ns re-garden.end-point
  (:require [org.httpkit.server :refer [run-server with-channel on-close on-receive send!]]
            [compojure.route :refer [resources not-found]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.handler :refer [site]]))


(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text"}
   :body    "Hello world"})

(defn- ws-handler [request]
  (with-channel request channel
               (on-close channel
                         (fn [status] (println "Chanel closed: " status)))
               (on-receive channel
                           (fn [data]
                             (prn data)
                             (send! channel (str "->" data))))))
(defroutes app-routes
           (resources "/")
           (GET "/ws" [] ws-handler)
           (not-found "<p> Page not found </p>"))

(defn -main []
  (run-server (site #'app-routes) {:port 3000}))

