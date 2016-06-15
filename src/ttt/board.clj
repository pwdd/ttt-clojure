(ns ttt.board
  (:require [ttt.helpers :as helpers]))

(def board-size 3)
(def board-length (* board-size board-size))
(def empty-spot :_)

(def winning-combos [
                     [0 1 2]
                     [3 4 5]
                     [6 7 8]

                     [0 3 6]
                     [1 4 7]
                     [2 5 8]

                     [0 4 8]
                     [2 4 6]
                    ])

(defn new-board
  []
  (vec (repeat board-length empty-spot)))

(defn move
  [board marker spot]
  (assoc board spot marker))

(defn is-available?
  [board spot]
  (= empty-spot (board spot)))

(defn is-full?
  [board first-marker second-marker]
  (every? #{first-marker second-marker} board))

(defn is-valid-move?
  [board spot]
  (and (helpers/in-range? spot board-length)
       (is-available? board spot)))

(defn winning-combo
  [board]
  (first
    (filter #(not (nil? %))
      (for [combo winning-combos]
        (if (and (= (board (combo 0))
                    (board (combo 1))
                    (board (combo 2)))
                  (not (= (board (combo 0)) empty-spot)))
          combo)))))

(defn winner
  [board]
  (if (winning-combo board)
    (let [combo (winning-combo board)]
       (board (combo 0)))))

(defn draw?
  [board first-marker second-marker]
  (and (is-full? board first-marker second-marker)
       (not (winner board))))

(defn game-over?
  [board first-marker second-marker]
  (or (draw? board first-marker second-marker)
      (not (nil? (winner board)))))
