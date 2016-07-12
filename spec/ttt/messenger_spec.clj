(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :refer :all]
            [ttt.player :refer [make-player]]
            [ttt.game :refer [create-game]]
            [ttt.board :refer [empty-spot]]))

(def human (make-player { :marker :x :role :human }))
(def easy-computer (make-player { :marker :o :role :easy-computer }))
(def hard-computer (make-player { :marker :h :role :hard-computer }))
(def game (create-game human human))
(def easy-x-human (create-game easy-computer human))
(def easy-x-easy (create-game easy-computer easy-computer))
(def hard-x-human (create-game hard-computer human))
(def hard-x-hard (create-game hard-computer hard-computer))

(describe "number-of-spaces"
  (it "returns the number of padding spaces for an empty string"
    (should= 60 (number-of-spaces 0 60)))
  (it "returns the number of spaces if message length is even"
    (should= 59 (number-of-spaces 2 60)))
  (it "returns the number of spaces if message length is odd"
    (should= 58 (number-of-spaces 3 60))))

(describe "padding-spaces"
  (it "returns an empty string if (/ message length 2.0) is equal to screen width"
    (should= "" (padding-spaces (clojure.string/join (repeat 120 "a")))))
  (it "returns a string with n repeated spaces"
    (should= "   " (padding-spaces (clojure.string/join (repeat 114 "a")))))
  (it "returns the right amount of spaces"
    (should= 44
             (count (padding-spaces "Please enter a number from 1-9: ")))))

(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x " (translate-keyword :x)))
  (it "returns ' o '"
    (should= " o " (translate-keyword :o)))
  (it "returns an empty space for :_"
    (should= "   " (translate-keyword empty-spot))))

(describe "stringify-board"
  (it "outputs a numbered representation of the board if no spot is taken"
    (should=
      "|   |---|---|---|   |---|---|---|   |"
      (clojure.string/join "" (map #(clojure.string/trim %)
      (clojure.string/split-lines
        (stringify-board [:_ :_ :_ :_ :_ :_ :_ :_ :_]))))))
  (it "combines numbers and letters when some spots are taken"
    (should=
      "| x |---|---|---o |   |---|---|---|   | x"
      (clojure.string/join "" (map #(clojure.string/trim %)
      (clojure.string/split-lines
        (stringify-board [:_ :x :_ :o :_ :_ :_ :_ :x]))))))
  (it "prints out a board that is full"
    (should=
      "x | x | o---|---|---o | x | o---|---|---x | o | x"
      (clojure.string/join "" (map #(clojure.string/trim %)
      (clojure.string/split-lines
        (stringify-board [:x :x :o :o :x :o :x :o :x])))))))

(describe "stringify-combo"
  (it "returns a string representing a vector of numbers"
    (should= "1, 2, 3" (stringify-combo [0 1 2])))
  (it "increases number by one"
    (should= "3, 5, 7" (stringify-combo [2 4 6]))))

(describe "result-human-computer"
  (it "returns tied message if the game ties"
    (should= "You tied\n" (result-human-computer [:x :o :x
                                                  :o :x :o
                                                  :o :x :o]
                                                  human
                                                  easy-computer)))
  (it "returns winning message if human player won"
    (should (re-find #"You won\W*(.*)"
                     (result-human-computer [:x :x :x
                                             :o :_ :o
                                             :o :x :o]
                                             human
                                             easy-computer))))
  (it "returns 'you lost' message if human player lost"
    (should (re-find #"You lost\W*(.*)"
                     (result-human-computer [:o :x :x
                                             :x :o :_
                                             :_ :_ :o]
                                             human
                                             easy-computer)))))

(describe "result"
 (it "returns 'tie' the game ends ties"
   (should= "The game tied" (result game
                                    [:x :o :x
                                     :o :x :o
                                     :o :x :o])))
 (it "returns Player X and winning positions if X is winner's marker"
   (should= "Player 'x' won on positions 1, 2, 3" (result game
                                                        [:x :x :x
                                                         :o :_ :o
                                                         :o :x :o])))
 (it "returns Player O and winning positions if O is winner's marker"
   (should= "Player 'o' won on positions 1, 5, 9" (result game
                                                        [:o :x :x
                                                         :x :o :_
                                                         :_ :_ :o]))))

(describe "moved-to"
  (context ":easy-x-human"
    (it "returns a message starting with 'You' if player is human"
      (should= "You moved to 2\n"
              (moved-to easy-x-human human 1)))
    (it "returns a message starting with 'Easy-computer' if player is easy-computer"
      (should= "Easy-computer moved to 2\n"
               (moved-to easy-x-human easy-computer 1))))

  (context ":hard-x-human"
    (it "returns a message starting with 'You' if player is human"
      (should= "You moved to 2\n"
              (moved-to hard-x-human human 1)))
    (it "returns a message starting with 'Hard-computer' if player is easy-computer"
      (should= "Hard-computer moved to 2\n"
                (moved-to hard-x-human hard-computer 1))))

  (context "players with same role"
    (it "returns message starting with 'Player [marker]' if player is human"
      (should= "Player 'x' moved to 2\n"
               (moved-to game human 1))))
    (it "returns message starting with 'Player [marker]' if player is easy-computer"
      (should= "Player 'o' moved to 3\n"
               (moved-to easy-x-easy easy-computer 2)))
    (it "returns message starting with 'Player [marker]' if player is easy-computer"
      (should= "Player 'h' moved to 9\n"
               (moved-to hard-x-hard hard-computer 8))))

(describe "print-message"
  (around [it]
    (with-out-str (it)))
  (it "outputs a message to stdout"
    (should= (str (padding-spaces "test message") "test message\r\n") (with-out-str (print-message "test message")))))

(describe "not-a-valid-number"
  (it "explains that empty space is not a number"
    (should= "Your choice is not valid. Empty spaces are not a number\n"
             (not-a-valid-number "")))
  (it "explains that a letter input is not a number"
    (should= "Your choice is not valid. 'a' is not a number\n"
             (not-a-valid-number "a"))))

(describe "not-a-valid-move"
  (it "explains that number is out of range"
    (should= "Your choice is not valid. There is no position 12 in the board\n"
             (not-a-valid-move 11))))

(describe "ask-user-number"
  (it "returns the user input"
    (should= "1" (with-in-str "1" (ask-user-number))))
  (it "trims out whitespaces from input"
    (should= "1" (with-in-str "  1 " (ask-user-number)))))

(describe "ask-player-marker"
  (it "returns the user input"
    (should= "x" (with-in-str "x" (ask-player-marker))))
  (it "trims out whitespaces from input"
    (should= "x" (with-in-str "  x " (ask-player-marker)))))

(describe "ask-player-role"
  (it "returns the user input"
    (should= "h" (with-in-str "h" (ask-player-role))))
  (it "trims out whitespaces from input"
    (should= "h" (with-in-str "  h " (ask-player-role))))
  (it "turns input into lowercase string"
    (should= "h" (with-in-str "H" (ask-player-role)))))

(describe "invalid-marker-msg"
  (it "explains that a word is an invalid input"
    (should= "Your choice is not valid. Marker must be a single letter."
             (invalid-marker-msg "foo" "")))
  (it "explains that a number is not a valid input"
    (should= "Your choice is not valid. Numbers and special characters are not accepted."
             (invalid-marker-msg "1" "")))
  (it "explains that a specil character is not a valid input"
    (should= "Your choice is not valid. Numbers and special characters are not accepted."
             (invalid-marker-msg "#" "")))
  (it "explains when a marker is taken"
    (should= "Your choice is not valid. This marker is taken by the first player."
             (invalid-marker-msg "x" "x"))))

(describe "stringify-role"
  (it "returns 'easy' if player is easy computer"
    (should= "easy" (stringify-role easy-computer)))
  (it "returns 'hard' if player is hard computer"
    (should= "hard" (stringify-role hard-computer)))
  (it "returns 'human' if player is human"
    (should= "human" (stringify-role human))))

(describe "write-game-type"
  (it "returns a keyword"
    (should (keyword? (write-game-type "easy" "human"))))
  (it "returns an alphabetically ordered keyword, does not matter the order of arguments"
    (should= :easy-x-hard (write-game-type "hard" "easy"))))
