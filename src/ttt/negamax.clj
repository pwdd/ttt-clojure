(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(def start-depth 0)
(def limit-depth 10)
(def start-alpha -100)
(def start-beta 100)

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

(defn scores
  [game board current-player opponent depth alpha beta]
  (let [spots (board/available-spots board)
        new-boards (map #(board/move board current-player %) spots)]
    (map #(- (negamax game
                      %
                      opponent
                      current-player
                      (inc depth)
                      (- beta)
                      (- alpha))) new-boards)))

(defn negamax-value
  [game board current-player opponent depth alpha beta]
  (if (board/game-over? board)
    (* (player/value current-player)
       (board-analysis game board current-player opponent depth))
    (let [value (apply max (scores game
                       board
                       current-player
                       opponent
                       depth
                       alpha
                       beta))
          max-value (apply max [value alpha])]
          (if (>= max-value beta)
            max-value
            value))))

(def negamax (memoize negamax-value))

(defn best-move
 [game board current-player opponent depth alpha beta]
 (if (board/is-empty? board)
   4
   (let [spots (board/available-spots board)
         scores (scores game board current-player opponent depth alpha beta)
         max-value (apply max scores)
         move (.indexOf scores max-value)]
     (nth spots move))))
