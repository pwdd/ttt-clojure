(ns ttt.rules
  (:require [ttt.board :as board]))

(defn- is-section-empty?
  [board-section]
  (and (= (first board-section) board/empty-spot)
       (apply = board-section)))

(defn- find-empty
  [board section]
  (loop [sections section]
    (let [first-part (first sections)
          board-section (mapv #(nth board %) first-part)]
      (if-not (nil? first-part)
        (if (is-section-empty? board-section)
          first-part
          (recur (rest sections)))))))

(defn find-empty-row
  [board]
  (find-empty board (board/board-rows (board/board-size board))))

(defn find-empty-column
  [board]
  (find-empty board (board/board-columns (board/board-size board))))

(defn find-empty-diagonal
  [board]
  (find-empty board (board/board-diagonals (board/board-size board))))

(defn- middle-spot
  [board-length]
  (let [middle (int (Math/floor (/ board-length 2)))]
    (if (odd? board-length)
      middle
      (- middle 2))))

(defn place-in-the-middle
  [board]
  (middle-spot (count board)))

(defn- corners
  [board-size]
  (let [rows (board/board-rows board-size)
        first-row (first rows)
        last-row (last rows)]
    (vector (first first-row) 
            (last first-row)
            (first last-row)
            (last last-row))))

(defn place-in-the-corner
  [board]
  (let [corners (corners (board/board-size board))]
    (first (filter #(= (nth board %) board/empty-spot) corners))))
