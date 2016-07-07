(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(def start-depth 0)
(def limit-depth 10)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Multimethod: board-value   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Multimethod: board-analysis   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti board-analysis
  (fn [game board first-player second-player depth]
    (:type game)))

(defmethod board-analysis :hard-x-hard
  [game board first-player second-player depth]
  (let [winner (board/winner-player board first-player second-player)]
  (cond
    (= winner first-player) (- 10 depth)
    (= winner second-player) (- depth 10)
    :else
      0)))

(defmethod board-analysis :default
  [game board first-player second-player depth]
  (let [winner (board/winner-player board first-player second-player)]
  (if winner
    (board-value winner depth)
    0)))

;;;;;;;;;;;;;;;;;
;;   Negamax   ;;
;;;;;;;;;;;;;;;;;

(declare negamax)

(defn negamax-scores
  [game board current-player opponent depth]
  (let [spots (board/available-spots board)
       new-boards (map #(board/move board current-player %) spots)]
    (map #(- (negamax game
                      %
                      opponent
                      current-player
                      (inc depth))) new-boards)))

(defn negamax-score
  [game board current-player opponent depth]
  (if (board/game-over? board)
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
  (if (board/is-empty? board)
    4
    (let [spots (board/available-spots board)
         scores (negamax-scores game board current-player opponent depth)
         max-value (apply max scores)
         best (.indexOf scores max-value)]
      (nth spots best))))
