(ns ttt.computer
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(def start-depth 0)

(defn computer-spot
  [board-length]
  (rand-int board-length))

(defn game-type
  [first-player second-player]
  (if (not (= (player/is-ai? first-player)
              (player/is-ai? second-player)))
    :human-computer))

(defn board-analysis
  [board first-player second-player depth]
  (if (= (game-type first-player second-player) :human-computer)
    (cond
      (board/is-winner-ai? board first-player second-player) (+ 10 depth)
      (not (board/is-winner-ai? board first-player second-player)) (+ -10 depth)
      :else
        0)
    (cond
      (= (board/winner-player board first-player second-player)
          first-player)
          (+ 10 depth)
      (= (board/winner-player board first-player second-player)
         second-player)
         (+ -10 depth)
      :else
      0)))

(declare negamax)

(defn scores
  [board current-player opponent depth]
  (let [spots (board/available-spots board)
        new-boards (map #(board/move board current-player %) spots)]
    (map #(- (negamax % opponent current-player (inc depth))) new-boards)))

(defn negamax-value
  [board current-player opponent depth]
  (if (board/game-over? board)
    (* (player/player-value current-player)
       (board-analysis board current-player opponent depth))
    (apply max (scores board current-player opponent depth))))

(def negamax (memoize negamax-value))

(defn best-move
  [board current-player opponent depth]
  (if (board/is-empty? board)
    4
    (let [spots (board/available-spots board)
          scores (scores board current-player opponent depth)
          max-value (apply max scores)
          move (.indexOf scores max-value)]
      (nth spots move))))
