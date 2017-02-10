(ns ttt.main
  (:require [ttt.game.loop :as game-loop])
  (:gen-class))

(defn -main
  [& args]
  (game-loop/play))
