(ns re-garden.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [re-garden.core-test]))

(doo-tests 're-garden.core-test)
