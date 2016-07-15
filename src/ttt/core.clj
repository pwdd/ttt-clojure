(ns ttt.core
  (:require [ttt.game-loop :as game-loop]))

; TODO test
(defn -main
  []
  (game-loop/play)
  )
