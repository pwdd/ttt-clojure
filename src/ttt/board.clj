(ns ttt.board
  (:require [ttt.helpers :as helpers]))

(def empty-spot :_)

(defn board-length
  [board-size]
  (* board-size board-size))

(defn new-board
  [board-size]
  (vec (repeat (board-length board-size) empty-spot)))

(defn board-rows
  [board-size]
  (mapv vec (partition board-size (range (board-length board-size)))))

(defn board-columns
  [board-size]
  (apply mapv vector (board-rows board-size)))

(defn- make-diagonal
  [indexes board-size]
  (->> indexes
       (map-indexed vector)
       (mapv #(get-in (board-rows board-size) %))))

(defn board-diagonals
  [board-size]
  (let [forward (range board-size)
        backward (reverse forward)]
    [(make-diagonal forward board-size) (make-diagonal backward board-size)]))

(defn winning-positions
  [board-size]
  (mapv vec (concat (board-rows board-size)
                    (board-columns board-size)
                    (board-diagonals board-size))))

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
  (and (helpers/in-range? spot (count board))
       (is-spot-available? board spot)))

(defn repeated-markers?
  [board combo]
  (let [selected-combo (mapv board combo)]
    (if-not (= empty-spot (first selected-combo))
      (apply = selected-combo))))

(defn winning-combo
  [board]
  (let [board-size (int (Math/sqrt (count board)))]
    (->> (winning-positions board-size)
         (filter #(repeated-markers? board %))
         (first))))
