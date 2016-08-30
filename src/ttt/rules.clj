(ns ttt.rules
  (:require [clojure.set :as set]
            [ttt.board :as board]
            [ttt.helpers :as helpers]))

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

(defn is-there-empty-section?
  [board]
  (or (find-empty-row board) 
      (find-empty-column board)
      (find-empty-diagonal board)))

(defn- middle-spot
  [board-length]
  (let [middle (int (Math/floor (/ board-length 2)))]
    (if (odd? board-length)
      middle
      (- middle 2))))

(defn place-in-the-middle
  [board]
  (middle-spot (count board)))

(defn corners
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
    (->> corners
         (filter #(= (nth board %) board/empty-spot))
         first)))

(defn is-board-with-one-move?
  [board]
  (and (= (count (board/available-spots board))
          (dec (count board)))))

(defn is-middle-free?
  [board]
  (let [middle (middle-spot (count board))]
    (= (nth board middle) board/empty-spot)))

(defn is-middle-the-best-move?
  [board]
  (or (board/is-board-empty? board)
       (and (is-board-with-one-move? board)
            (is-middle-free? board))))

(defn markers-frequency
  [board combo]
  (let [board-combo (mapv #(nth board %) combo)]
    (set/map-invert (frequencies board-combo))))

(defn- get-key-by-value
  [map-collection value]
  (first (keep #(when (= (val %) value) (key %)) 
               map-collection)))

(defn marker-frequency
  [board combo marker]
  (let [markers-frequency (markers-frequency board combo)
        marker-count (get-key-by-value markers-frequency marker)]
    (if marker-count
      marker-count
      0)))

(defn- empty-spot-frequency
  [board combo]
  (marker-frequency board combo board/empty-spot))

(defn- missing-one?
  [board combo marker]
  (= (marker-frequency board combo marker)
     (dec (board/board-size board))))

(defn missing-only-one?
  [board combo marker]
  (let [emptys (empty-spot-frequency board combo)]
    (and (= emptys 1)
         (missing-one? board combo marker))))

(defn where-can-win
  [board marker]
  (let [winning-combos (board/winning-positions (board/board-size board))]
    (first (filter #(missing-only-one? board % marker) winning-combos))))

(defn place-in-winning-spot
  [board marker]
  (let [winning-combo (where-can-win board marker)
        combo-in-board (mapv #(nth board %) winning-combo)
        indexed (zipmap winning-combo combo-in-board)]
    (get-key-by-value indexed board/empty-spot)))

(defn only-same-markers?
  [board combo current-player-marker opponent-marker]
  (and (>= (marker-frequency board combo current-player-marker) 1)
       (zero? (marker-frequency board combo opponent-marker))))

(defn owned-sections
  [board current-player-marker opponent-marker]
  (let [sections (board/winning-positions (board/board-size board))]
    (filterv #(only-same-markers? board % current-player-marker opponent-marker) 
             sections)))

(defn most-populated-owned-section
  [board current-player-marker opponent-marker]
  (let [marker-frequencies 
         (mapv #(marker-frequency board % current-player-marker) 
               (owned-sections board current-player-marker opponent-marker))
        highest-rate (apply max marker-frequencies)
        highest-rate-index (.indexOf marker-frequencies highest-rate)]
    (nth (owned-sections board current-player-marker opponent-marker) 
         highest-rate-index)))

(defn available-spots-in-section
  [board section]
  (let [board-section (mapv #(nth board %) section)
        board-map (zipmap section board-section)]
    (keep #(when (= (val %) board/empty-spot) (key %)) 
          board-map)))

(defn- owned
  [board current-player-marker opponent-marker]
  (most-populated-owned-section board current-player-marker opponent-marker))

(defn- empty-section
  [board]
  (is-there-empty-section? board))

(defn play-based-on-rules
  [player params]
  (let [board (:board params)
        current-player-marker (get-in params [:current-player :marker])
        opponent-marker (get-in params [:opponent :marker])
        board-size (board/board-size board)]

    (cond
      (is-middle-the-best-move? board) (place-in-the-middle board)
      (and (is-board-with-one-move? board)
           (not (is-middle-free? board)))
        (helpers/random-move (available-spots-in-section
                               board 
                               (corners board-size)))
      (where-can-win board current-player-marker) 
        (place-in-winning-spot board current-player-marker) 
      (where-can-win board opponent-marker)
        (place-in-winning-spot board opponent-marker)
      (not (empty? (owned-sections board 
                                   current-player-marker 
                                   opponent-marker)))
        (helpers/random-move 
          (available-spots-in-section board 
                                     (owned board 
                                            current-player-marker
                                            opponent-marker)))
      (is-there-empty-section? board)
        (helpers/random-move (empty-section board))
      :else
        (helpers/random-move (board/available-spots board)))))
