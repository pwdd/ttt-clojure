(ns ttt.computer
  (:require [ttt.board :as board]))

(defn set-max
  [max number]
  (if (nil? max)
    number
    (if (not (nil? max))
      (if (> number max)
        number
        max))))

(defn computer-spot
  [board-length]
  (rand-int board-length))
