(ns re-garden.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
  :name
  (fn [db]
    (reaction (:name @db))))

(re-frame/register-sub
  :applications
  (fn [db]
    (reaction (:applications @db))))

(re-frame/register-sub
  :sessions
  (fn [db]
    (reaction (:sessions @db))))

(re-frame/register-sub
  :active-panel
  (fn [db _]
    (reaction (:active-panel @db))))
