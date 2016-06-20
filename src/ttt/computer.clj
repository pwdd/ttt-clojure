(ns ttt.computer)

(defn get-computer-spot
  [board-length]
  (rand-int board-length))
