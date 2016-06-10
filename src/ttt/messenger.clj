(ns ttt.messenger
  (:require [ttt.helpers :refer [to-string]]
            [ttt.board :refer [board-size]]))

(def separator "\n---|---|---\n")

(def instructions (str 
                     "First player will be playing with 'X'.\n"
                     "The board is represented bellow:\n")
)

(defn board-representation [board]
  (str
    (clojure.string/join separator
      (let [board (partition board-size (map to-string board))]
        (for [combo board]
          (clojure.string/join "|" combo)
        )
      )
    )
    "\n"
  )
)