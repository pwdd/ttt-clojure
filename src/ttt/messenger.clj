(ns ttt.messenger
  (:require [ttt.board :as board]))

(def separator "\n---|---|---\n")

(def welcome (str "   |------------------------|"
                  "\n---| Welcome to Tic Tac Toe |---\n"
                  "   |------------------------|\n"))

(def instructions (str
                     "\nFirst player will be playing with 'X'.\n"
                     "The board is represented like the following:\n"))

(def ask-first-player "\nShould player 'X' be a human or a computer?")

(def ask-second-player "\nShould player 'O' be a human or a computer?")

(def h-or-c "Please type H (human) or C (computer):")

(def choose-a-number "Please enter a number from 1-9: ")

(def board-representation
  " 1 | 2 | 3 \n---|---|---\n 4 | 5 | 6 \n---|---|---\n 7 | 8 | 9 \n")

(defn translate-keyword
  [k]
  (if (not (= k board/empty-spot))
    (str " " (name k) " ")
    "   "))

(defn print-board
  [board]
  (str
    (clojure.string/join separator
      (let [board (partition board/board-size
                             (map translate-keyword board))]
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

(defn result-human-computer
  [board first-player second-player]
  (cond
    (board/draw? board) "You tied\n"
    (board/is-winner-ai? board first-player second-player)
      (str "You lost.\n"
           "Computer won on positions "
           (print-combo (board/winning-combo board))
            "\n")
    :else
      (str "You won!\n"
      "Winning positions: "
      (print-combo (board/winning-combo board))
      "\n")))

(defn result
  [board]
 (if (board/draw? board)
   "The game tied"
   (str "Player "
        (clojure.string/upper-case (name (board/winner board)))
        " won on positions "
        (print-combo (board/winning-combo board)))))

(defn moved-to
 [player spot]
 (if (:is-ai? player)
   (str "Computer moved to " (inc spot))
   ""))
