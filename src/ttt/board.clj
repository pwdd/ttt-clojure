(ns ttt.board)

(def board-size 3)
(def board-length (* board-size board-size))
(def empty-spot :_)

(def winning-positions
  [
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

(defn is-empty-spot?
  [marker]
  (= marker empty-spot))

(defn move
  [board marker spot]
  (assoc board spot marker))

(defn is-available?
  [board spot]
  (= empty-spot (get board spot)))

(defn is-full?
  [board]
  (not (some is-empty-spot? board)))

(defn available-spots
  [board]
  (map first
    (filter #(= (second %) empty-spot)
            (map-indexed vector board))))

(defn in-range?
  [number]
  (and (>= number 0) (<= number board-length)))

(defn is-valid-move?
  [board spot]
  (and (in-range? spot) (is-available? board spot)))

(defn get-rows
  [board]
  (vec (partition board-size board)))

(defn get-cols
  [board]
  (vector
    (take-nth 3 board)
    (take-nth 3 (drop 1 board))
    (take-nth 3 (drop 2 board))))

(defn get-diagonals
  [board]
  (vector
    (take-nth 4 board)
    (take-nth 2 (drop-last 2 (drop 2 board)))))

(defn get-combos
  [board]
  (into [] (concat (get-rows board)
                   (get-cols board)
                   (get-diagonals board))))

(defn winner
  [board]
  (let [combos (get-combos board)]
    (let [preds
         (for [combo combos]
           (apply = combo))
         ]
         (if (>= (.indexOf preds true) 0)
           (let [marker
                 (nth (get combos (.indexOf preds true)) 0)
                ]
             (if (not (= marker empty-spot))
               marker))))))

; (defn winner-two
;   [board]
;   (loop [combo winning-positions]
;     (if (and (= (get board (get combo 0))
;                 (get board (get combo 1))
;                 (get board (get combo 2)))
;              (not (= (get board (get combo 0)) empty-spot)))
;       (combo 0))
;   ))
