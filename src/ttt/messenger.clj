(ns ttt.messenger
  (:require [ttt.board :as board]
            [ttt.player :as player]
            [ttt.helpers :as helpers]))

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
  "\nPlease enter a single letter that will be the first player's marker")

(def ask-second-marker
  "\nPlease enter a single letter that will be the second player's marker")

(def choose-a-number "Please enter a number from 1-9: ")

(def board-representation
  " 1 | 2 | 3 \n---|---|---\n 4 | 5 | 6 \n---|---|---\n 7 | 8 | 9 \n")

(def default-invalid-input "\nYour choice is not valid. ")

(def invalid-role-msg
  (str default-invalid-input
    "Only (H) human, (EC) easy computer and (HC) hard computer are available"))

(defn translate-keyword
  [k]
  (if (not (= k board/empty-spot))
    (str " " (name k) " ")
    "   "))

(defn stringfy-board
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

(defn stringfy-combo
  [combo]
  (clojure.string/join ", " (map #(inc %) combo)))

(defn human-lost
  [board]
  (str "You lost.\n"
       "Computer won on positions "
       (stringfy-combo (board/winning-combo board))
        "\n"))

(defn human-won
  [board]
  (str "You won!\n"
  "Winning positions: "
  (stringfy-combo (board/winning-combo board))
  "\n"))

(defn default-win
  [board]
  (str "Player "
       (clojure.string/upper-case (name (board/winner-marker board)))
       " won on positions "
       (stringfy-combo (board/winning-combo board))))

(defn result-human-computer
  [board first-player second-player]
  (cond
    (board/draw? board) "You tied\n"
    (board/is-winner-ai? board first-player second-player)
      (human-lost board)
    :else
      (human-won board)))

(defmulti result
  (fn [game & [board first-player second-player]]
    (:type game)))

(defmethod result :human-x-easy
  [game & [board first-player second-player]]
  (result-human-computer board first-player second-player))

(defmethod result :human-x-hard
  [game & [board first-player second-player]]
  (result-human-computer board first-player second-player))

(defmethod result :default
  [game & [board first-player second-player]]
  (if (board/draw? board)
    "The game tied"
    (default-win board)))

(defn moved-to
 [player spot]
 (if (player/is-ai? player)
   (str (clojure.string/capitalize
          (name (player/role player)))
          " moved to "
          (inc spot)
          "\n")
   ""))

(defn print-message
  [msg]
  (println msg))

(defn not-a-valid-number
  [input]
  (cond
    (empty? input)
      (str default-invalid-input "Empty spaces are not a number")
    :else
      (str default-invalid-input "'" input "' is not a number")))

(defn not-a-valid-move
  [position]
  (cond
    (not (helpers/in-range? position board/board-length))
      (str default-invalid-input "There is no position " (inc position) " in the board")
    :else
      (str default-invalid-input "The position is taken")))

(defn ask-user-number
  []
  (clojure.string/trim (read-line)))

(defn ask-player-marker
  []
  (clojure.string/trim (read-line)))

(defn ask-player-role
  []
  (helpers/clean-string (read-line)))

(defn invalid-marker-msg
  [input opponent-marker]
  (cond
    (> (count input) 1)
      (str default-invalid-input "Marker must be a single letter.")
    (not (re-matches #"^[a-zA-Z]$" input))
      (str default-invalid-input
           "Numbers and special characters are not accepted.")
    (= input opponent-marker)
      (str default-invalid-input "This marker is taken by the first player.")
    :else
      (str default-invalid-input "Only a letter from 'a' to 'z' is valid.")))
