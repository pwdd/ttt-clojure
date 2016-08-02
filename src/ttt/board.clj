(ns ttt.board
  (:require [ttt.helpers :as helpers]))

(def board-size 3)
(def board-length (* board-size board-size))
(def empty-spot :_)

(defn new-board
  []
  (vec (repeat board-length empty-spot)))

(defn board-rows
  []
  (partition board-size (range board-length)))

(defn board-columns
  []
  (apply map vector (board-rows)))

(defn diagonals
  [start-column next-column]
  (loop [row 0
         column start-column
         diagonal []]
    (if (>= row board-size)
      diagonal
      (recur (inc row)
             (next-column column)
             (conj diagonal
               (get-in (mapv vec (board-rows)) [row column]))))))

(defn board-diagonals
  []
  [(diagonals 0 inc) (diagonals (dec board-size) dec)])

(defn winning-positions
  []
  (mapv vec (concat (board-rows) (board-columns) (board-diagonals))))

(defn move
  [board spot marker]
  (assoc board spot marker))

(defn is-board-full?
  [board]
  (not-any? #(= empty-spot %) board))

(defn is-board-empty?
  [board]
  (every? #(= empty-spot %) board))

(defn is-spot-available?
  [board spot]
  (= empty-spot (board spot)))

(defn available-spots
  [board]
  (keep-indexed #(if (= empty-spot %2) %1) board))

(defn is-valid-move?
  [board spot]
  (and (helpers/in-range? spot board-length)
       (is-spot-available? board spot)))

(defn repeated-markers?
  [board combo]
  (let [selected-combo (mapv board combo)]
    (if (not (= empty-spot (first selected-combo)))
      (apply = selected-combo))))

(defn winning-combo
  [board]
  (->> (winning-positions)
       (filter #(repeated-markers? board %))
       (first)))
