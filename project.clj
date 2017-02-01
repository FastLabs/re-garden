(defproject re-garden "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.456"]
                 [reagent "0.5.1"]
                 [re-frame "0.6.0"]
                 [re-com "0.7.0"]
                 [secretary "1.2.3"]
                 [proto-repl "0.3.1"]
                 [ring/ring-core "1.5.1"]
                 [javax.servlet/servlet-api "2.5"]
                 [http-kit "2.2.0"]
                 [org.clojure/java.jdbc "0.7.0-alpha1"]
                 [compojure "1.5.2"]
                 [garden "1.3.0"]]



  :profiles {:dev
             {:dependencies [[org.clojure/test.check "0.9.0"]]}}

  :main ^:skip-aot re-garden.end-point

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.9"]
            [lein-garden "0.2.6"]
            [lein-doo "0.1.7"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"
                                    "resources/public/css/compiled"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   re-garden.css/screen
                     :compiler     {:output-to     "resources/public/css/compiled/screen.css"
                                    :pretty-print? true}}]}

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs"]
                        :figwheel     {:on-jsload "re-garden.core/mount-root"}
                        :compiler     {:main                 re-garden.core
                                       :output-to            "resources/public/js/compiled/app.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :asset-path           "js/compiled/out"
                                       :source-map-timestamp true}}

                       {:id           "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :compiler     {:output-to     "resources/public/js/compiled/test.js"
                                       :output-dir    "resources/public/js/compiled/test/out"
                                       :main          re-garden.runner
                                       :optimizations :none}}

                       {:id           "min"
                        :source-paths ["src/cljs"]
                        :compiler     {:main            re-garden.core
                                       :output-to       "resources/public/js/compiled/app.js"
                                       :optimizations   :advanced
                                       :closure-defines {goog.DEBUG false}
                                       :pretty-print    false}}]})
