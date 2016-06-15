(ns ttt.user
  (:require [ttt.helpers :refer [is-int? input-to-number]]
            [ttt.board :refer [is-valid-move?]]
            [ttt.messenger :refer [choose-a-number]]))

(defn get-spot
  []
  (let [input (read-line)]
    (if (is-int? input)
      (input-to-number input))))

; TODO test
(defn get-valid-input
  [board]
  (println choose-a-number)
  (let [input (get-spot)]
    (if (is-valid-move? board input)
      input
      (recur board))))
