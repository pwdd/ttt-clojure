(ns ttt.messenger
  (:require [ttt.helpers :refer [translate-keyword]]
            [ttt.board :refer [board-size]]))

(def separator "\n---|---|---\n")

(def instructions (str
                     "First player will be playing with 'X'.\n"
                     "The board is represented bellow:\n")
)

(def board-representation
  " 1 | 2 | 3 \n---|---|---\n 4 | 5 | 6 \n---|---|---\n 7 | 8 | 9 \n")

(defn print-board
  [board]
  (str
    (clojure.string/join separator
      (let [board (partition board-size (map translate-keyword board))]
        (for [combo board]
          (clojure.string/join "|" combo)
        )
      )
    )
    "\n"
  )
)
