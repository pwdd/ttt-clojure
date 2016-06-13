(ns ttt.core
  (:require [ttt.board :refer :all]
            ;[ttt.user :refer [get-spot]]
            [ttt.messenger :refer [print-board]]))

(def first-player :x)
(def second-player :o)

(defn -main
  []
  (let [board (atom (new-board))]
    (println (print-board @board))))
