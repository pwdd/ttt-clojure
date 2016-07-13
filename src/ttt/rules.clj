(ns ttt.rules
  (:require [ttt.player :as player]
            [ttt.board :as board]))

(defn draw?
  [board]
  (and (board/is-board-full? board)
       (not (player/winner-marker board))))

(defn game-over?
  [board]
  (or (draw? board)
      (not (nil? (board/winning-combo board)))))
