(ns ttt.core
  (:require [ttt.game-loop :as game-loop]))

; TODO test
(defn -main
  []
  (game-loop/play))
; Don't need to test this if it's just wrapping another function, and so long as `game-loop/play` is
; tested.
