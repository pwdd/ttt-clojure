(ns ttt.board)

(def board-size 3)
(def board-length (* board-size board-size))

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

(defn board []
  (vec (range board-length))
)

(defn move [board marker spot]
  (assoc board spot marker))

(defn move-two [args]
  (assoc (:board args) 
         (:spot args) 
         (:marker args)))

(defn is-available? [board spot]
  (integer? (get board spot)))

(defn is-full? [board]
  (not (some integer? board)))

(defn available-spots [board]
  (filterv integer? board))

(defn in-range? [number]
  (and (>= number 0) (<= number board-length))
)

(defn is-valid-move? [board spot]
  (and (in-range? spot) (is-available? board spot))
)