(ns ttt.board
  (:require [ttt.helpers :as helpers]
            [ttt.player :as player]))

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
  [board player spot]
  (assoc board spot (player/marker player)))

(defn is-available?
  [board spot]
  (= empty-spot (board spot)))

(defn is-full?
  [board]
  (not-any? #(= empty-spot %) board))

(defn is-empty?
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
       (is-available? board spot)))

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

(defn winner-marker
  [board]
  (if (winning-combo board)
    (let [combo (winning-combo board)]
       (board (combo 0)))))

(defn winner-player
  [board first-player second-player]
  (let [winner (winner-marker board)]
    (cond
      (= (player/marker first-player) winner) first-player
      (= (player/marker second-player) winner) second-player
      :else
        false)))

(defn is-winner-ai?
 [board first-player second-player]
 (let [winner (winner-player board first-player second-player)]
   (player/is-ai? winner)))

(defn draw?
  [board]
  (and (is-full? board)
       (not (winner-marker board))))

(defn game-over?
  [board]
  (or (draw? board)
      (not (nil? (winning-combo board)))))
