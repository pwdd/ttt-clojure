(ns ttt.rules
  (:require [ttt.board :as board]
            [ttt.helpers :as helpers]))

(defn- correspondent-board-combo
  [board indexes-combo]
  (mapv #(nth board %) indexes-combo))

(defn- is-combo-empty?
  [board-combo]
  (and (= (first board-combo) board/empty-spot)
       (apply = board-combo)))

(defn find-empty-combos
  [board indexes-combos]
  (loop [combos indexes-combos
         empty-combos []]
    (let [first-combo (first combos)
          board-combo (correspondent-board-combo board first-combo)]
      (cond
        (nil? first-combo) empty-combos
        (is-combo-empty? board-combo)
          (recur (rest combos) (conj empty-combos first-combo))
        :else
          (recur (rest combos) empty-combos)))))

(defn- get-an-empty-combo
  [board]
  (let [board-size (board/board-size board)
        winning-positions (board/winning-positions board-size)]
    (first (find-empty-combos board winning-positions))))

(defn- is-there-empty-combos?
  [board]
  (not (nil? (get-an-empty-combo board))))

(defn- center-spot
  [board-length]
  (let [center (int (Math/floor (/ board-length 2)))]
    (if (odd? board-length)
      center
      (- center 2))))

(defn place-in-the-center
  [board]
  (center-spot (count board)))

(defn corners
  [board-size]
  (let [rows (board/board-rows board-size)
        first-row (first rows)
        last-row (last rows)]
    (vector (first first-row)
            (last first-row)
            (first last-row)
            (last last-row))))

(defn- is-board-with-one-move?
  [board]
  (and (= (count (board/available-spots board))
          (dec (count board)))))

(defn- is-center-free?
  [board]
  (let [center (center-spot (count board))]
    (= (nth board center) board/empty-spot)))

(defn is-center-the-best-move?
  [board]
  (or (board/is-board-empty? board)
       (and (is-board-with-one-move? board)
            (is-center-free? board))))

(defn is-corner-the-best-move?
  [board]
 (and (is-board-with-one-move? board)
      (not (is-center-free? board))))

(defn available-spots-in-combo
  [board combo]
  (let [board-combo (correspondent-board-combo board combo)
        board-map (zipmap combo board-combo)]
    (helpers/get-keys-by-value board-map board/empty-spot)))

(defn place-in-a-corner
  [board]
  (let [corners (corners (board/board-size board))]
    (helpers/random-move (available-spots-in-combo board corners))))

(defn- markers-to-token
  [marker]
  (if (= marker board/empty-spot)
    marker
    (:token marker)))

(defn markers-frequency
  [board combo]
  (let [board-combo (correspondent-board-combo board combo)]
    (frequencies (map markers-to-token board-combo))))

(defn- frequency
  [marker-count]
  (if-not (nil? marker-count)
    marker-count
    0))

(defn- empty-spot-frequency
  [board combo]
  (let [markers-frequency (markers-frequency board combo)
        empty-spots-count (board/empty-spot markers-frequency)]
    (frequency empty-spots-count)))

(defn marker-frequency
  [board combo marker]
  (if (= marker board/empty-spot)
    (empty-spot-frequency board combo)
  (let [markers-frequency (markers-frequency board combo)
        token (:token marker)
        marker-count (token markers-frequency)]
    (frequency marker-count))))

(defn- missing-one?
  [board combo marker]
  (= (marker-frequency board combo marker)
     (dec (board/board-size board))))

(defn can-win-in-a-combo?
  [board combo marker]
  (let [emptys (empty-spot-frequency board combo)]
    (and (= emptys 1)
         (missing-one? board combo marker))))

(defn combo-to-win
  [board marker]
  (let [winning-combos (board/winning-positions (board/board-size board))]
    (first (filter #(can-win-in-a-combo? board % marker) winning-combos))))

(defn can-win?
  [board marker]
  (seq (combo-to-win board marker)))

(defn place-in-winning-spot
  [board marker]
  (let [winning-combo (combo-to-win board marker)
        combo-in-board (correspondent-board-combo board winning-combo)
        indexed (zipmap winning-combo combo-in-board)]
    (first (helpers/get-keys-by-value indexed board/empty-spot))))

(defn only-same-markers?
  [board combo current-player-marker opponent-marker]
  (and (>= (marker-frequency board combo current-player-marker) 1)
       (zero? (marker-frequency board combo opponent-marker))))

(defn owned-combos
  [board current-player-marker opponent-marker]
  (let [combos (board/winning-positions (board/board-size board))]
    (filterv #(only-same-markers? board % current-player-marker opponent-marker)
             combos)))

(defn most-populated-owned-combo
  [board current-player-marker opponent-marker]
  (let [marker-frequencies
         (mapv #(marker-frequency board % current-player-marker)
               (owned-combos board current-player-marker opponent-marker))
        highest-rate (apply max marker-frequencies)
        highest-rate-index (.indexOf marker-frequencies highest-rate)]
    (nth (owned-combos board current-player-marker opponent-marker)
         highest-rate-index)))

(defn- has-combos?
  [board current-player-marker opponent-marker]
  (seq (owned-combos board current-player-marker opponent-marker)))

(defn- fill-in-a-combo
  [board current-player-marker opponent-marker]
  (helpers/random-move
    (available-spots-in-combo board
                              (most-populated-owned-combo board
                                                          current-player-marker
                                                          opponent-marker))))

(defn play-based-on-rules
  [game-params]
  (let [board (:board game-params)
        current-player-marker (get-in game-params [:current-player :marker])
        opponent-marker (get-in game-params [:opponent :marker])
        board-size (board/board-size board)]

    (cond
      (is-center-the-best-move? board) (place-in-the-center board)
      (is-corner-the-best-move? board) (place-in-a-corner board)
      (can-win? board current-player-marker)
        (place-in-winning-spot board current-player-marker)
      (can-win? board opponent-marker)
        (place-in-winning-spot board opponent-marker)
      (has-combos? board current-player-marker opponent-marker)
        (fill-in-a-combo board current-player-marker opponent-marker)
      (is-there-empty-combos? board)
        (helpers/random-move (get-an-empty-combo board))
      :else
        (helpers/random-move (board/available-spots board)))))
