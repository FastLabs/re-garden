(ns snapshot.spec
  (:require [clojure.spec :as s]))

(s/def ::snap-status #{:pending :final :failed})
(s/def ::snap-type #{:daily :weekly :monthly :quarterly :yearly})

(s/def ::snap-id string?)
(s/def ::status ::snap-status)
(s/def ::type ::snap-type)
(s/def ::start-time inst?)
(s/def ::end-time inst?)


(s/def ::snapshot (s/keys :req [::snap-id ::status ::type] :opt []))
