(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(def start-depth 0)

(defn computer-spot
  [board-length]
  (rand-int board-length))

(defn game-type
  [first-player second-player]
  (cond
    (not (= (player/is-ai? first-player)
            (player/is-ai? second-player))) :human-computer
    (and (or (= :easy-computer (player/role first-player))
             (= :easy-computer (player/role second-player)))
         (or (= :hard-computer (player/role first-player))
             (= :hard-computer (player/role second-player)))) :easy-hard
    :else
      :computer-computer))

(defn board-analysis
  [board first-player second-player depth]
  (let [winner (board/winner-player board first-player second-player)]
  (cond
    (= :human-computer (game-type first-player second-player))
      (cond
        (= :human (player/role winner)) (+ -10 depth)
        (or (= :easy-computer (player/role winner))
            (= :hard-computer (player/role winner))) (+ 10 depth)
        :else
          0)
    (= :easy-hard (game-type first-player second-player))
      (cond
        (= :easy-computer (player/role winner)) (+ -10 depth)
        (= :hard-computer (player/role winner)) (+ 10 depth)
        :else
        0)
    :else
      (cond
        (= winner first-player) (+ 10 depth)
        (= winner second-player) (+ -10 depth)
      :else
        0))))

(declare negamax)

(defn scores
  [board current-player opponent depth]
  (let [spots (board/available-spots board)
        new-boards (map #(board/move board current-player %) spots)]
    (map #(- (negamax % opponent current-player (inc depth))) new-boards)))

(defn negamax-value
  [board current-player opponent depth]
  (if (board/game-over? board)
    (* (player/value current-player)
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
