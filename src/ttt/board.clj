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

(defn is-spot-available?
  [board spot]
  (= empty-spot (board spot)))

(defn is-board-full?
  [board]
  (not-any? #(= empty-spot %) board))

(defn is-board-empty?
  [board]
  (every? #(= empty-spot %) board))

(defn available-spots
  [board]
  (map first
    (filter #(= empty-spot (second %))
      (map-indexed vector board))))

(defn is-valid-move?
  [board spot]
  (and (helpers/in-range? spot board-length)
       (is-spot-available? board spot)))

(defn repeated?
  [board combo]
  (let [selected-combo (for [idx combo] (nth board idx))]
    (if (not-any? #{empty-spot} selected-combo)
      (apply = selected-combo))))

(defn find-repetition
  [board]
  (filter #(repeated? board %) winning-combos))

(defn winning-combo
  [board]
  (first (find-repetition board)))
