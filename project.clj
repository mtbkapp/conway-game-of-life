(defproject game-of-life "0.1.0-SNAPSHOT"
  :description "An implmentation of Conway's game of life"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot game-of-life.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
