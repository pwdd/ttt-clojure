(ns ttt.rules
  (:require [ttt.board :as board]))

(defn find-empty-row
  [board]
  (loop [rows (board/board-rows (board/board-size board))]
    (let [row (first rows)
          board-row (mapv #(nth board %) row)]
      (if (and (= (first board-row) board/empty-spot)
               (apply = board-row))
        row
        (recur (rest rows))))))
