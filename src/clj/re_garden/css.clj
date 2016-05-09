(ns re-garden.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "black"}]
  [:.level1 {:color "green"}]
  [:h1 :h2 [:a {:color "orange"}]])
