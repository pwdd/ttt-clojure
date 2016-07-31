(ns ttt.messenger
  (:require [ttt.board :as board]
            [ttt.player :as player]
            [ttt.helpers :as helpers]
            [ttt.rules :as rules]
            [ttt.input-validation :as input-validation]
            [clojure.string :as string]))

(def separator "\n---|---|---\n")

(def welcome
  (str "   |------------------------|\n"
       "  ---| Welcome to Tic Tac Toe |---\n"
       "   |------------------------|"))

(def instructions "The board is represented like the following:\n")

(def new-or-saved-msg (str "Would you like to (1) restart a saved game "
                           "or (2) start a new game? "
                           "Please enter 1 or 2:"))

(def role-options-msg
  "Please type H (human), EC (easy computer) or HC (hard computer): ")

(defn ask-role-msg
  [marker]
  (str "Should player '"
       marker
       "' be a human or a computer?\n"
       role-options-msg))

(def ask-first-marker-msg
  "Please enter a single letter that will be the first player's marker")

(def ask-second-marker-msg
  "Please enter a single letter that will be the second player's marker")

(def choose-a-number "Please enter a number from 1-9: ")

(def board-representation
  " 1 | 2 | 3 \n---|---|---\n 4 | 5 | 6 \n---|---|---\n 7 | 8 | 9 \n")

(defn translate-keyword
  [k]
  (if (not (= k board/empty-spot))
    (str " " (name k) " ")
    "   "))

(defn stringify-board
  [board]
  (str
    (string/join separator
      (let [board (partition board/board-size
                             (map translate-keyword board))]
        (for [combo board]
          (string/join "|" combo))))
    "\n"))

(defn stringify-combo
  [combo]
  (string/join ", " (map #(inc %) combo)))

(def default-invalid-input "Your choice is not valid. ")

(def invalid-role-options-msg
  (str default-invalid-input
    "Only (H) human, (EC) easy computer and (HC) hard computer are available"))

(defn human-lost
  [board]
  (str "You lost.\n"
       "Computer won on positions "
       (stringify-combo (board/winning-combo board))
       "\n"))

(defn human-won
  [board]
  (str "You won!\n"
       "Winning positions: "
       (stringify-combo (board/winning-combo board))
       "\n"))

(defn default-win
  [board]
  (str "Player '"
       (name (rules/winner-marker board))
       "' won on positions "
       (stringify-combo (board/winning-combo board))))

(defmulti result
  (fn [game & [board first-player second-player]]
    (:player-roles game)))

(defmethod result :computer-x-human
  [game & [board first-player second-player]]
  (cond
    (rules/draw? board) "You tied\n"
    (rules/is-winner-ai? board first-player second-player)
      (human-lost board)
    :else
      (human-won board)))

(defmethod result :default
  [game & [board first-player second-player]]
  (if (rules/draw? board)
    "The game tied"
    (default-win board)))

(defmulti moved-to
  (fn [game player spot]
    (:player-roles game)))

(defmethod moved-to :computer-x-human
  [game player spot]
  (if (player/is-ai? player)
    (str (string/capitalize
           (name (player/role player)))
           " moved to "
           (inc spot)
           "\n")
    (str "You moved to " (inc spot) "\n")))

(defmethod moved-to :same-player-roles
  [game player spot]
  (str "Player '"
       (name (player/marker player))
       "' moved to "
       (inc spot)
       "\n"))

(defmethod moved-to :default
  [game player spot]
  (str (string/capitalize
         (name (player/role player)))
       " moved to "
       (inc spot)
       "\n"))

(defn not-a-valid-number
  [input]
  (cond
    (empty? input)
      (str default-invalid-input "Empty spaces are not a number\n")
    :else
      (str default-invalid-input "'" input "' is not a number\n")))

(defn not-a-valid-move
  [position]
  (cond
    (not (helpers/in-range? position board/board-length))
      (str default-invalid-input
           "There is no position "
           (inc position)
           " in the board\n")
    :else
      (str default-invalid-input "The position is taken\n")))

(defn wrong-number-msg
  [board input]
  (if (input-validation/is-int? input)
    (not-a-valid-move (helpers/input-to-number input))
    (not-a-valid-number input)))

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

(defn current-player-is
  [current-player-marker]
  (str "Current player is playing with '"
       current-player-marker
       "'"))

(def choose-a-file-msg "Enter the name of the saved game you wanna play:")

(defn display-files-list
  [files-list]
  (string/join ", " files-list))
