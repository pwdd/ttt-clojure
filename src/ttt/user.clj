(ns ttt.user
  (:require [ttt.helpers :as helpers]
            [ttt.board :as board]
            [ttt.messenger :as messenger]))

(defn get-spot
  []
  (let [input (read-line)]
    (if (helpers/is-int? input)
      (helpers/input-to-number input))))

; TODO test
(defn get-valid-input
  [board]
  (println messenger/choose-a-number)
  (let [input (get-spot)]
    (if (board/is-valid-move? board input)
      input
      (recur board))))
