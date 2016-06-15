(ns ttt.messenger
  (:require [ttt.helpers :as helpers]
            [ttt.board :as board]))

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
      (let [board (partition board/board-size (map helpers/translate-keyword board))]
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
  (clojure.string/join ", " (map #(inc %) combo)))

(defn result
  [board]
  (cond
    (board/draw? board) "You tied\n"
    (= (board/winner board) :x)
      (str "You won!\n"
      "Winning positions: "
      (print-combo (board/winning-combo board))
      "\n")
    :else
      (str "You lost.\n"
           "Computer won on positions "
           (print-combo (board/winning-combo board))
            "\n")))

(defn moved-to
 [player spot]
 (if (= (player :type) "computer")
   (str "Computer moved to " (inc spot))
   ""))
