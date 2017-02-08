(ns task.spec
  (:require [clojure.spec :as s]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::description string?)
(s/def ::task-action #{:no-action :exec-action})
(s/def ::task-type #{:simple :etl-invoke})
(s/def ::value string?)
(s/def ::matcher string?)

;;(s/def ::task-attr-def (s/tuple [::name? ::task-attribute? ::mandatory?]))
;;(s/def ::sub-task-def (s/coll-of ::task-def))

(s/def ::pre-conditions (s/keys :req [::name]
                                :opt [::value ::matcher]))
(s/def ::depends-on fn?)
(s/def ::sub-defs (s/coll-of ::task-def))

(s/def ::task-def (s/keys :req [::task-type]
                          :opt [::sub-defs ::description ::depends-on ::task-action]))


(s/def ::sub-tasks (s/coll-of ::task-inst))
(s/def ::task-status #{:started :failed :completed :scheduled :pending})
(s/def ::task-inst (s/keys :req [::id ::task-def ::task-status]
                           :opt [::task-def]))

