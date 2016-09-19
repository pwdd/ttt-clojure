(ns ttt.messenger
  (:require [clojure.string :as string]
            [ttt.boards.board :as board]
            [ttt.player :as player]
            [ttt.helpers :as helpers]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.input-validation :as input-validation]
            [ttt.colors :as colors]))

(def board-color :red)
(def number-color :gray)

(defn- newline-wrap
  [message]
  (str "\n" message "\n"))

(defn separator
  [board-size]
  (let [dashes "---"
        pipe "|"]
   (->> (repeat board-size dashes)
        (string/join pipe)
        newline-wrap
        (colors/colorize board-color))))

(def welcome
  (str "|------------------------|\n"
       "---| " (colors/colorize :purple "Welcome to Tic Tac Toe") " |---\n"
       "|------------------------|"))

(def new-or-saved-msg (str "Would you like to\n"
                           "(1) restart a saved game or\n"
                           "(2) start a new game?\n"
                           "Please enter 1 or 2:"))

(def choose-board-size
  (str "Would you like to play using a board that has dimensions:\n"
       "(3) 3 x 3\n"
       "(4) 4 x 4\n"
       "(5) 5 x 5?\n"
       "Please enter '3', '4', or '5'"))

(def role-options-msg
  "Please type H (human), EC (easy computer), MC (medium computer) or HC (hard computer): ")

(defn- marker-string
  [marker]
  (name (:token marker)))

(defn- colorful-marker
  [marker]
  (colors/colorize (:color marker) (marker-string marker)))

(defn colorful-empty-spot
  [number-string]
  (colors/colorize number-color number-string))

(defn ask-role-msg
  [marker]
  (str "Should player '"
       (colorful-marker marker)
       "' be a human or a computer?\n"
       role-options-msg))

(def ask-first-marker-msg
  "Please enter a single letter that will be the first player's marker")

(def ask-second-marker-msg
  "Please enter a single letter that will be the second player's marker")

(def choose-a-number "Please enter a number corresponding to a position on the board: \n")

(def or-enter-save "-  'SAVE' to save the current game")
(def or-enter-quit "- 'QUIT' to quit the game")
(def or-restart "- 'RESTART' to restart this game")
(def other-options
  (string/join "\n" [or-enter-save or-enter-quit or-restart]))

(def multiple-choice
  (str choose-a-number
       "\nYou can also type: \n"
       other-options))

(def give-file-name "Enter the name of the game as you want it to be saved:")

(def file-already-exists-msg
  (str "There is already a file with this name."))

(def overwrite-file-option
    (str "Would you like to (1) overwrite it or (2) choose another name?\n"
    "Please enter 1 or 2"))

(def game-saved "Your game has been saved.")

(defn- empty-spot-to-number
  [index]
  (if (< index 9)
    (colorful-empty-spot (str " " (inc index) " "))
    (colorful-empty-spot (str " " (inc index)))))

(defn translate-keyword
  [[index marker]]
  (if (= marker board/empty-spot)
    (empty-spot-to-number index)
    (str " " (colorful-marker marker) " ")))

(defn join-combo
  [string-combo]
  (string/join (str (colors/colorize board-color "|"))
                    string-combo))

(defn translate-board
  [board]
  (let [indexed-board (map-indexed vector board)
        board-string (partition (board/board-size board) 
                                (map translate-keyword indexed-board))]
    (map join-combo board-string)))

(defn- joined-board
  [board]
  (string/join (separator (board/board-size board))
               (translate-board board)))

(defn stringify-board
  [board]
  (str (joined-board board) "\n"))

(defn stringify-combo
  [combo]
  (string/join ", " (map inc combo)))

(def default-invalid-input "Your choice is not valid. ")

(def invalid-role-options-msg
  (str default-invalid-input
       "Only H (human), EC (easy computer), MC (medium-computer) and HC (hard computer) are available"))

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
  [board first-player second-player]
  (let [winner (evaluate-game/winner-player board first-player second-player)
        winner-marker (:marker winner)]
    (str "Player '"
        (colorful-marker winner-marker) 
         "' won on positions "
         (stringify-combo (board/winning-combo board)))))

(defmulti result
  (fn [game & [board first-player second-player]]
    (:player-roles game)))

(defmethod result :computer-x-human
  [game & [board first-player second-player]]
  (cond
    (evaluate-game/draw? board) "You tied\n"
    (evaluate-game/is-winner-ai? board first-player second-player)
      (human-lost board)
    :else
      (human-won board)))

(defmethod result :default
  [game & [board first-player second-player]]
  (if (evaluate-game/draw? board)
    "The game tied"
    (default-win board first-player second-player)))

(defmulti moved-to
  (fn [game player spot]
    (:player-roles game)))

(defmethod moved-to :computer-x-human
  [game player spot]
  (if (player/is-ai? (:role player))
    (str (string/capitalize (name (:role player)))
         " moved to "
         (inc spot)
         "\n")
    (str "You moved to " (inc spot) "\n")))

(defmethod moved-to :same-roles
  [game player spot]
  (str "Player '"
      (colorful-marker (:marker player))
       "' moved to "
       (inc spot)
       "\n"))

(defmethod moved-to :default
  [game player spot]
  (str (string/capitalize (name (:role player))) " moved to " (inc spot) "\n"))

(defn not-a-valid-number
  [input]
  (if (empty? input)
    (str default-invalid-input "Empty spaces are not a number\n")
    (str default-invalid-input "'" input "' is not a number\n")))

(defn not-a-valid-move
  [position board-length]
  (if-not (helpers/in-range? position board-length)
    (str default-invalid-input
         "There is no position "
         (inc position)
         " in the board\n")
    (str default-invalid-input "The position is taken\n")))

(defn wrong-number-msg
  [board input]
  (if (input-validation/is-int? input)
    (not-a-valid-move (helpers/input-to-number input) (count board))
    (not-a-valid-number input)))

(defn invalid-marker-msg
  [input opponent-marker]
  (cond
    (> (count input) 1)
      (str default-invalid-input "Marker must be a single letter.")
    (not (re-matches #"^[a-zA-Z]$" input))
      (str default-invalid-input "Numbers and special characters are not accepted.")
    (= input opponent-marker)
      (str default-invalid-input "This marker is taken by the first player.")
    :else
      (str default-invalid-input "Only a letter from 'a' to 'z' is valid.")))

(defn current-player-is
  [current-player-marker]
  (str "Current player is playing with '"
      (colorful-marker current-player-marker) "'"))

(def choose-a-file-msg "Enter the name of the saved game you wanna play:")

(defn display-files-list
  [files-list]
  (string/join ", " files-list))

(defn board-after-invalid-input
  [board input]
  (str (wrong-number-msg board input) (stringify-board board)))

(def show-saved-files-msg "These are the saved files:")

(defn list-saved-files
  [filenames]
  (str show-saved-files-msg
       "\n"
       (display-files-list filenames)
       "\n"
       choose-a-file-msg))
