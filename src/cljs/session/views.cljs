(ns session.views
  (:require [re-com.core :as rc]
            [re-com.util :as rcu]
            [reagent.core :as ra]))

(def tab-defs [
               {:id ::tab1 :label "Tab 1" :say-this "First app"}
               {:id ::tab2 :label "Tab 2" :say-this "Second app"}])

(defn tab-header
  [{:keys [label] :as tab-def}]
  [:span (str label "-")])

(def session {})

(defn sessions-tab
  [sessions]
  (map (fn [{:keys [session-id]}] {:id session-id :label session-id :say-this session-id}) sessions))


(defn session-container [_]
  (let [sel-tab-id (ra/atom nil)
        change-tab-fn #(reset! sel-tab-id %)]
    (fn [sessions active-session]
      (prn sessions)
      (if (seq sessions)
        [rc/v-box
         :gap "20px"
         :children [
                    [rc/horizontal-tabs
                     :model sel-tab-id
                     :tabs tab-defs
                     :label-fn tab-header
                     :on-change change-tab-fn]
                    [:div [:p (:say-this (rcu/item-for-id @sel-tab-id tab-defs))]]]]
        [:div "no apps"]))))

