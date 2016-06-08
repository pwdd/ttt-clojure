(ns ttt.core
  (:require [ttt.board :refer :all]))

(def first-player "X")
(def second-player "O")

(defn board-representation []
  (println " 1 | 2 | 3 ")
  (println "---|---|---")
  (println " 4 | 5 | 6 ")
  (println "---|---|---")
  (println " 7 | 8 | 9 \n"))

(defn instructions []
  (println "First player will be playing with 'X'.\n")
  (println "The board is represented bellow:\n")
  (board-representation))

(defn -main []
  (instructions)
  )


