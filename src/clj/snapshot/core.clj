(ns snapshot.core
  (:require [snapshot.spec :as snap]
            [clojure.spec :as s]))

(defn new-snapshot
  [id]
  {:pre [(s/valid? ::snap/id id)]
   :post [(s/valid? ::snap/snapshot %)]}
  {})

