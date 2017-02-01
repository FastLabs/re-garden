(ns re-garden.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [re-com.core :as re-com]
            [ws.core :as ws]))

;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

(defonce ws-chan (atom  (ws/new-sock (str "ws://" (.-host js/location) "/ws"))))

(defn send [msg]
  (.send @ws-chan msg))

(defn msg-sender []
  [:div [:button {:on-click #(send "Click1")} "Send"]])


(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "go to About Page"
   :href "#/about"])

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [msg-sender] [link-to-about-page]]])


;; about

(defn about-title []
  '[re-com/title
    :label "This is the About Page." :level :level1]
  [:h1 [:a "hello"]])

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
  [:div.navbar "Hello world"])


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


