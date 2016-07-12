(ns ttt.messenger
  (:require [ttt.board :as board]
            [ttt.player :as player]
            [ttt.helpers :as helpers]))

(def half-screen 60)
(def lines 5)

(defn clear-screen
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H"))
  (print (clojure.string/join (repeat lines "\n"))))

(defn number-of-spaces
  [message-length & [optional-half-screen]]
  (let [half-message (int (Math/ceil (/ message-length 2.0)))]
    (if (not (nil? optional-half-screen))
      (- optional-half-screen half-message)
      (- half-screen half-message))))

(defn padding-spaces
  [message]
  (clojure.string/join
    (repeat (number-of-spaces (count message)) " ")))

(def separator (str (padding-spaces "---|---|---\n") "---|---|---\n"))

(def welcome
  (str "\n"
       (padding-spaces "   |------------------------|")
                       "   |------------------------|\n"
       (padding-spaces "---| Welcome to Tic Tac Toe |---")
                       " ---| Welcome to Tic Tac Toe |---\n"
       (padding-spaces "   |------------------------|")
                       "   |------------------------|\n"))

(def instructions "The board is represented like the following:\n")

(def role-msg
  "Please type H (human), EC (easy computer) or HC (hard computer): ")

(defn ask-role
  [marker]
  (str "\n"
       (padding-spaces "Should player 'x' be a human or a computer?")
       "Should player '"
       marker
       "' be a human or a computer?\n"
       (padding-spaces role-msg)
       role-msg))

(def ask-first-marker
  "Please enter a single letter that will be the first player's marker")

(def ask-second-marker
  "Please enter a single letter that will be the second player's marker")

(def choose-a-number "Please enter a number from 1-9: ")

(def board-representation
  " 1 | 2 | 3 \n---|---|---\n 4 | 5 | 6 \n---|---|---\n 7 | 8 | 9 \n")

(defn build-board-representation
  []
  (str "\n"
    (padding-spaces " 1 | 2 | 3 ") " 1 | 2 | 3 \n"
    (padding-spaces "---|---|---\n") "---|---|---\n"
    (padding-spaces " 4 | 5 | 6 \n") " 4 | 5 | 6 \n"
    (padding-spaces "---|---|---\n") "---|---|---\n"
    (padding-spaces " 7 | 8 | 9 \n") " 7 | 8 | 9 \n"))

(def default-invalid-input "Your choice is not valid. ")

(def invalid-role-msg
  (str default-invalid-input
    "Only (H) human, (EC) easy computer and (HC) hard computer are available"))

(defn translate-keyword
  [k]
  (if (not (= k board/empty-spot))
    (str " " (name k) " ")
    "   "))

(defn stringify-board
  [board]
  (str
    (clojure.string/join separator
      (let [board (partition board/board-size
                             (map translate-keyword board))]
        (for [combo board]
          (str (padding-spaces (clojure.string/join "|" combo))
               (clojure.string/join "|" combo) "\n"))))
    "\n"))

(defn stringify-combo
  [combo]
  (clojure.string/join ", " (map #(inc %) combo)))

(defn human-lost
  [board]
  (str "\n"
    (padding-spaces "You lost.") "You lost.\n"
    (padding-spaces "Computer won on positions 1, 2, 3")
         "Computer won on positions "
         (stringify-combo (board/winning-combo board))
         "\n"))

(defn human-won
  [board]
  (str "\n"
    (padding-spaces "You won!") "You won!\n"
    (padding-spaces "Winning positions: 1, 2, 3")
         "Winning positions: "
         (stringify-combo (board/winning-combo board))
         "\n"))

(defn default-win
  [board]
  (str "Player '"
       (name (board/winner-marker board))
       "' won on positions "
       (stringify-combo (board/winning-combo board))))

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

(defmethod result :easy-x-human
  [game & [board first-player second-player]]
  (result-human-computer board first-player second-player))

(defmethod result :hard-x-human
  [game & [board first-player second-player]]
  (result-human-computer board first-player second-player))

(defmethod result :default
  [game & [board first-player second-player]]
  (if (board/draw? board)
    "The game tied"
    (default-win board)))

(defn computer-human-moved-to
 [player spot]
 (if (player/is-ai? player)
   (str (clojure.string/capitalize
          (name (player/role player)))
          " moved to "
          (inc spot)
          "\n")
   (str "You moved to " (inc spot) "\n")))

(defn same-player-type-moved-to
  [player spot]
  (str "Player '"
       (name (player/marker player))
       "' moved to "
       (inc spot)
       "\n"))

(defmulti moved-to
  (fn [game player spot]
    (:type game)))

(defmethod moved-to :easy-x-human
  [game player spot]
  (computer-human-moved-to player spot))

(defmethod moved-to :hard-x-human
  [game player spot]
  (computer-human-moved-to player spot))

(defmethod moved-to :human-x-human
  [game player spot]
  (same-player-type-moved-to player spot))

(defmethod moved-to :hard-x-hard
  [game player spot]
  (same-player-type-moved-to player spot))

(defmethod moved-to :easy-x-easy
  [game player spot]
  (same-player-type-moved-to player spot))

(defmethod moved-to :default
  [game player spot]
  (str (clojure.string/capitalize
         (name (player/role player)))
       " moved to "
       (inc spot)
       "\n"))

(defn print-message
  [msg]
  (println (str (padding-spaces msg) msg)))

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

(defn centralize-cursor
  []
  (do (print (padding-spaces "")) (flush)))

(defn ask-user-number
  []
  (centralize-cursor)
  (let [number (clojure.string/trim (read-line))]
    (clear-screen)
    number))

(defn ask-player-marker
  []
  (centralize-cursor)
  (let [marker (clojure.string/trim (read-line))]
    (clear-screen)
    marker))

(defn ask-player-role
  []
  (centralize-cursor)
  (let [role (helpers/clean-string (read-line))]
    (clear-screen)
    role))

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

(defn stringify-role
  [player]
  (if (= :human (player/role player))
    "human"
    (let [role (name (player/role player))
         limit (.indexOf role "-")]
      (subs role 0 limit))))

(defn write-game-type
  [first-name second-name]
  (keyword (clojure.string/join "-x-"(sort [first-name second-name]))))

(defn make-board-disappear
  [player]
  (if (or (= :easy-computer (player/role player))
          (= :hard-computer (player/role player)))
    (do (Thread/sleep 1000)
        (clear-screen))))
