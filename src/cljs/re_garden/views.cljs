(ns re-garden.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [re-com.core :as re-com]
            [app-meta.views :as app-view]
            [session.views :as session-view]
            [ws.core :as ws]))

;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

;(defonce ws-chan (atom  (ws/new-sock (str "ws://" (.-host js/location) "/ws"))))

;(defn send [msg]
;  (.send @ws-chan msg))

(defn msg-sender []
  [:div [:button
         {:on-click #(re-frame/dispatch
                       [:set-applications
                        [{:app-name "app -1" :id "app- 1"}
                         {:app-name "app -2" :id "app- 2"}]])}
         "Load Applications"]])


(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "go to About Page"
   :href "#/about"])

(defn root-view
      [apps sessions]
  [re-com/h-box
   :children [[re-com/box :child [app-view/app-container apps]]
              [re-com/box :child [session-view/session-container sessions]]]])

(defn home-panel []
  (let [apps (re-frame/subscribe [:applications])
        sessions (re-frame/subscribe [:sessions])]
    (fn []
      [re-com/v-box
       :gap "1em"
       :children [[home-title]
                  [msg-sender]
                  [root-view @apps @sessions]
                  [link-to-about-page]]])))


;; about

(defn about-title []
  [re-com/title
    :label "This is the About Page." :level :level1])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [:div [link-to-home-page] [:div "hello"]]]])

(defn header
  []
  [:div.navbar "Application Manager"])


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[header]
                  (panels @active-panel)]])))


