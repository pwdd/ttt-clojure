(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.player :as player]
            [ttt.rules :as rules]))

(def start-depth 0)
(def limit-depth 10)

(defmulti board-value (fn [player depth] (:role player)))

(defmethod board-value :human
  [player depth]
  (- depth 10))

(defmethod board-value :easy-computer
  [player depth]
  (- depth 10))

(defmethod board-value :hard-computer
  [player depth]
  (- 10 depth))

(defmulti board-analysis
  (fn [game board first-player second-player depth]
    (:type game)))

(defmethod board-analysis :hard-x-hard
  [game board first-player second-player depth]
  (let [winner (player/winner-player board first-player second-player)]
  (cond
    (= winner first-player) (- 10 depth)
    (= winner second-player) (- depth 10)
    :else
      0)))

(defmethod board-analysis :default
  [game board first-player second-player depth]
  (let [winner (player/winner-player board first-player second-player)]
  (if winner
    (board-value winner depth)
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
    (* (player/value current-player)
       (board-analysis game board current-player opponent depth))
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
