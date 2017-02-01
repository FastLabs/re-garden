(ns re-garden.core
  (:require [clojure.spec :as s]
            [clojure.string :as str])
  (:import (java.util Date)))

(s/def ::task-type #{:WEEKCLY :QUATERLY :YEARLY})
;;(s/def ::job)

(defn new-task [task-type]
  {:task-type  task-type
   :start-time (Date.)})

(str/join ", " ["1" "2"])