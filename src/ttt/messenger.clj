(ns ttt.messenger
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(def separator "\n---|---|---\n")

(def welcome (str "   |------------------------|"
                  "\n---| Welcome to Tic Tac Toe |---\n"
                  "   |------------------------|\n"))

(def instructions "The board is represented like the following:\n")

(def role-msg
  "Please type H (human), EC (easy computer) or HC (hard computer): ")

(defn ask-role
  [marker]
  (println (str "\nShould player '"
                marker
                "' be a human or a computer?\n"
                role-msg)))

(def ask-first-marker
  "\nPlease enter a letter that will be the first player's marker")

(def ask-second-marker
  "\nPlease enter a single letter that will be the second player's marker")

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
        (clojure.string/upper-case (name (board/winner-mark board)))
        " won on positions "
        (print-combo (board/winning-combo board)))))

(defn moved-to
 [player spot]
 (if (player/is-ai? player)
   (str (clojure.string/capitalize
          (name (player/role player)))
          " moved to "
          (inc spot)
          "\n")
   ""))
