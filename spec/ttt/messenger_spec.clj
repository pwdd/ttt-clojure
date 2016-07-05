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

(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x " (translate-keyword :x)))
  (it "returns ' o '"
    (should= " o " (translate-keyword :o)))
  (it "returns an empty space for :_"
    (should= "   " (translate-keyword empty-spot))))

(describe "stringfy-board"
  (it "outputs a numbered representation of the board if no spot is taken"
    (should=
      "   |   |   \n---|---|---\n   |   |   \n---|---|---\n   |   |   \n"
      (stringfy-board [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "combines numbers and letters when some spots are taken"
    (should=
      "   | x |   \n---|---|---\n o |   |   \n---|---|---\n   |   | x \n"
      (stringfy-board [:_ :x :_ :o :_ :_ :_ :_ :x]))))

(describe "stringfy-combo"
  (it "returns a string representing a vector of numbers"
    (should= "1, 2, 3" (stringfy-combo [0 1 2])))
  (it "increases number by one"
    (should= "3, 5, 7" (stringfy-combo [2 4 6]))))

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
    (should (re-find #"You lost\W*(.*)" (result-human-computer [:o :x :x
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
  (it "returns empty string if player is human"
    (should (empty? (moved-to human 1))))
  (it "returns a message to where computer moved incremented by one"
    (should-not (empty? (moved-to easy-computer 3)))))

(describe "print-message"
  (around [it]
    (with-out-str (it)))
  (it "outputs a message to stdout"
    (should= "test message\r\n" (with-out-str (print-message "test message")))))

(describe "not-a-valid-number"
  (it "explains that empty space is not a number"
    (should= "\nYour choice is not valid. Empty spaces are not a number"
             (not-a-valid-number "")))
  (it "explains that a letter input is not a number"
    (should= "\nYour choice is not valid. 'a' is not a number"
             (not-a-valid-number "a"))))

(describe "not-a-valid-move"
  (it "explains that number is out of range"
    (should= "\nYour choice is not valid. There is no position 12 in the board"
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
    (should= "\nYour choice is not valid. Marker must be a single letter."
             (invalid-marker-msg "foo" "")))
  (it "explains that a number is not a valid input"
    (should= "\nYour choice is not valid. Numbers and special characters are not accepted."
             (invalid-marker-msg "1" "")))
  (it "explains that a specil character is not a valid input"
    (should= "\nYour choice is not valid. Numbers and special characters are not accepted."
             (invalid-marker-msg "#" "")))
  (it "explains when a marker is taken"
    (should= "\nYour choice is not valid. This marker is taken by the first player."
             (invalid-marker-msg "x" "x"))))

(describe "stringfy-role"
  (it "returns 'easy' if player is easy computer"
    (should= "easy" (stringfy-role easy-computer)))
  (it "returns 'hard' if player is hard computer"
    (should= "hard" (stringfy-role hard-computer)))
  (it "returns 'human' if player is human"
    (should= "human" (stringfy-role human))))
