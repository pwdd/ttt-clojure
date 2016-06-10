(ns ttt.core
  (:require [ttt.board :refer :all]
            [ttt.messenger :refer [board-representation]]))

(def first-player :x)
(def second-player :o)

(defn -main []
  (println "message")
  (println (board-representation (board)))
  (move (board) :x 1)
  (println (board-representation (board)))
)


