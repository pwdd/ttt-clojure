(ns ttt.core
  (:require [ttt.game-loop :as game-loop]))

(defn -main
  []
  (game-loop/play))
