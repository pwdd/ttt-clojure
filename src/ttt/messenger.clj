(ns ttt.messenger
  (:require [ttt.helpers :refer [translate-keyword]]
            [ttt.board :refer [board-size draw? winner winning-combo]]))

(def separator "\n---|---|---\n")

(def instructions (str
                     "\nFirst player will be playing with 'X'.\n"
                     "The board is represented like the following:\n"))

(def choose-a-number "Please enter a number from 1-9: ")

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

(defn print-combo
  [combo]
  (vec (map #(inc %) combo)))

(defn result
  [board]
  (if (draw? board)
    "tie"
    (str "Player "
         (clojure.string/upper-case (name (winner board)))
         " won on positions "
         (print-combo (winning-combo board)))))
