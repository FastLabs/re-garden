(ns app-meta.views
  (:require [re-frame.core :as rf]
            [re-com.core :refer [h-box v-box box input-text]]
            [reagent.core :as ra]
            [clojure.string :as str]))


(defn app-item
  [{:keys [:on-click]} {:keys [app-name] :as app}]
  [:li {:on-click #(when on-click (apply on-click app))
        :style    {:cursor "pointer"}}
   [:span app-name]])

(defn app-list
  [{:keys [on-app-selected]} apps]
  [:ul (map (fn [app] ^{:key (:id app) } [app-item {:on-click on-app-selected} app] ) apps)])

(defn- app-matcher [search-criteria]
  (fn [{:keys [app-name]}]
    (or (str/blank? search-criteria)
        (str/includes? app-name search-criteria))))

(defn start-app [app]
  (rf/dispatch [:start-app app]))


(defn app-container [_]
  (let [search (ra/atom "")]
    (fn [apps]
      (let [display-apps (filter (app-matcher @search) apps)] ;TODO: review the matcher as it would be generated for every on-change
        [v-box
         :children [
                    [box
                    ; :max-width "100px"
                     :child [input-text
                             :change-on-blur? false
                             :on-change #(reset! search %)
                             :model search]]
                    [box :child [app-list
                                 {:on-app-selected start-app}
                                 display-apps]]]]))))