(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.evaluate-game :as evaluate-game]))

(def start-depth 0)

(defn board-analysis
  [board current-player-marker opponent-marker depth]
  (if (evaluate-game/draw? board)
    0
    (let [winner (evaluate-game/winner-marker board)]
      (if (= winner current-player-marker)
        (- 10 depth)
        (- depth 10)))))

(declare negamax)

(defn scores
  [board current-player-marker opponent-marker depth]
  (let [spots (board/available-spots board)
       new-boards (map #(board/move board
                                    %
                                    current-player-marker) spots)]
    (map #(- (negamax % opponent-marker current-player-marker (inc depth)))
         new-boards)))

(defn negamax-score
  [board current-player-marker opponent-marker depth]
  (if (evaluate-game/game-over? board)
    (board-analysis board current-player-marker opponent-marker depth)
    (apply max (scores board
                       current-player-marker
                       opponent-marker
                       depth))))

(def negamax (memoize negamax-score))

(defn best-move
  [board current-player-marker opponent-marker depth]
  (if (board/is-board-empty? board)
    4
    (let [spots (board/available-spots board)
         scores (scores board current-player-marker opponent-marker depth)
         max-value (apply max scores)
         best (.indexOf scores max-value)]
      (nth spots best))))
