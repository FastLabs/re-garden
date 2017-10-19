(ns task.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::description string?)
(s/def ::task-action #{:no-action :exec-action})
(s/def ::value string?)
(s/def ::matcher string?)

(s/def ::pre-conditions (s/keys :req [::name]
                                :opt [::value ::matcher]))

(s/def ::sub-defs (s/coll-of ::task-def))

(s/def ::dep-def (s/coll-of (s/cat :name keyword? :predicate fn?)))
(s/def ::deps (s/coll-of (s/cat :name keyword? :meet? boolean?)))
(s/def ::key-attrs (s/coll-of keyword?))

;;TODO: is the dependency definition manadory? for a middle layer task is not mandator but for a terminal yes
(s/def ::task-def (s/keys :req [::name]
                          :opt [::sub-defs ::description ::dep-def
                                ::task-action ::key-attrs]))


(s/def ::sub-tasks (s/coll-of ::task-inst))
(s/def ::task-status #{:started :failed :completed :scheduled :pending})
(s/def ::task-inst (s/keys :req [::id ::task-def ::task-status]
                           :opt [::deps]))

