(ns ttt.board)

(def board-size 9)
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

(defn new-board []
  { 
    :board-struct (vec (repeat board-size :_))
  }
)

