(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.rules :as rules]))

(def start-depth 0)

(defn board-analysis
  [board current-player-marker opponent-marker depth]
  (if (rules/draw? board)
    0
    (let [winner (rules/winner-marker board)]
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
    (map #(- (negamax %
                      opponent-marker
                      current-player-marker
                      (inc depth))) new-boards)))

(defn negamax-score
  [board current-player-marker opponent-marker depth]
  (if (rules/game-over? board)
    (board-analysis board current-player-marker opponent-marker depth)
    (apply max (scores board
                       current-player-marker
                       opponent-marker
                       depth))))

(def negamax (memoize negamax-score))
; This is cool :) I did not know about clojure's `memoize` function.

(defn best-move
  [board current-player-marker opponent-marker depth]
  (if (board/is-board-empty? board)
    4
    (let [spots (board/available-spots board)
         scores (scores board current-player-marker opponent-marker depth)
         max-value (apply max scores)
         best (.indexOf scores max-value)]
      (nth spots best))))
; This isn't something I have a 100% solid opinion on, but my thought is that you probably shouldn't
; define something within a let block unless it's used more than once.
