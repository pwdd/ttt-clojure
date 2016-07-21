(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.player :as player]
            [ttt.rules :as rules]))

(def start-depth 0)

(defn board-analysis
  [board current-player opponent depth]
  (let [winner (player/winner-player board current-player opponent)]
    (cond
      (= winner current-player) (- 10 depth)
      (= winner opponent) (- depth 10)
      :else
        0)))

(declare negamax)

(defn negamax-scores
  [game board current-player opponent depth]
  (let [spots (board/available-spots board)
       new-boards (map #(board/move board
                                    (player/marker current-player)
                                    %) spots)]
    (map #(- (negamax game
                      %
                      opponent
                      current-player
                      (inc depth))) new-boards)))

(defn negamax-score
  [game board current-player opponent depth]
  (if (rules/game-over? board)
    (board-analysis board current-player opponent depth)
    (apply max (negamax-scores game
                               board
                               current-player
                               opponent
                               depth))))

(def negamax (memoize negamax-score))

(defn best-move
  [game board current-player opponent depth]
  (if (board/is-board-empty? board)
    4
    (let [spots (board/available-spots board)
         scores (negamax-scores game board current-player opponent depth)
         max-value (apply max scores)
         best (.indexOf scores max-value)]
      (nth spots best))))
