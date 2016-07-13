(ns ttt.core
  (:require [ttt.game-loop :as game-loop]
            [ttt.prompt :as prompt]))

; TODO test
(defn -main
  []
  (game-loop/play)
  )
