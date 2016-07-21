(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :refer :all]
            [ttt.player :as player]
            [ttt.game :as game]
            [ttt.board :as board]))

(describe "ask-role-msg"
  (it "returns a message that contains a player's marker"
    (should (re-find #" 'x' " (ask-role-msg "x")))))

(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x " (translate-keyword :x)))
  (it "returns ' o '"
    (should= " o " (translate-keyword :o)))
  (it "returns an empty space for :_"
    (should= "   " (translate-keyword board/empty-spot))))

(describe "stringify-board"
  (it "outputs a representation of the empty board"
    (should=
      "   |   |   \n---|---|---\n   |   |   \n---|---|---\n   |   |   \n"
      (stringify-board (board/new-board))))
  (it "combines empty spaces and letters when some spots are taken"
    (should=
      "   | x |   \n---|---|---\n o |   |   \n---|---|---\n   |   | x \n"
      (stringify-board [:_ :x :_ :o :_ :_ :_ :_ :x]))))

(describe "stringify-combo"
  (it "returns a string representing a vector of numbers"
    (should= "1, 2, 3" (stringify-combo [0 1 2])))
  (it "increases number by one"
    (should= "3, 5, 7" (stringify-combo [2 4 6]))))

(describe "result"
  (context ":default"
    (with game (game/create-game :human :human))
    (it "returns a message saying that the game ends ties"
      (should= "The game tied" (result @game
                                      [:x :o :x
                                       :o :x :o
                                       :o :x :o])))
    (it "returns a message that has the marker 'x' and the winning positions"
      (should= "Player 'x' won on positions 1, 2, 3" (result @game
                                                          [:x :x :x
                                                           :o :_ :o
                                                           :o :x :o])))
    (it "returns a message that has the marker 'o' and the winning positions"
      (should= "Player 'o' won on positions 1, 5, 9" (result @game
                                                          [:o :x :x
                                                           :x :o :_
                                                           :_ :_ :o]))))

  (context ":computer-x-human"
    (with game (game/create-game :human :easy-computer))
    (with human (player/make-player { :marker :x :role :human }))
    (with easy-computer (player/make-player { :marker :o :role :easy-computer }))
    (it "returns tied message if the game ties"
      (should= "You tied\n" (result @game
                                    [:x :o :x
                                     :o :x :o
                                     :o :x :o]
                                     @human
                                     @easy-computer)))
    (it "returns winning message if human player won"
      (should (re-find #"You won(.*)"
                       (result @game
                               [:x :x :x
                                :o :_ :o
                                :o :x :o]
                                @human
                                @easy-computer))))
    (it "returns 'you lost' message if human player lost"
      (should (re-find #"You lost(.*)"
                       (result @game
                               [:o :x :x
                                :x :o :_
                                :_ :_ :o]
                                @human
                                @easy-computer))))))

(describe "moved-to"
  (with human (player/make-player { :marker :x :role :human }))
  (with easy-computer (player/make-player { :marker :o :role :easy-computer }))
  (with hard-computer (player/make-player { :marker :h :role :hard-computer }))
  (with human-x-human (game/create-game :human :human))
  (with easy-x-human (game/create-game :easy-computer :human))
  (with easy-x-easy (game/create-game :easy-computer :easy-computer))
  (with hard-x-human (game/create-game :hard-computer :human))
  (with hard-x-hard (game/create-game :hard-computer :hard-computer))
  (context ":computer-x-human"
    (it "returns a message starting with 'You' if player is human"
      (should= "You moved to 2\n"
              (moved-to @easy-x-human @human 1)))
    (it "returns a message starting with 'Easy-computer' if player is easy-computer"
      (should= "Easy-computer moved to 2\n"
               (moved-to @easy-x-human @easy-computer 1))))

  (context ":same-player-roles"
    (it "returns message starting with 'Player [marker]' if player is human"
      (should= "Player 'x' moved to 2\n"
               (moved-to @human-x-human @human 1))))
    (it "returns message starting with 'Player [marker]' if player is easy-computer"
      (should= "Player 'o' moved to 3\n"
               (moved-to @easy-x-easy @easy-computer 2)))
    (it "returns message starting with 'Player [marker]' if player is hard-computer"
      (should= "Player 'h' moved to 9\n"
               (moved-to @hard-x-hard @hard-computer 8))))

(describe "not-a-valid-number"
  (it "explains that empty space is not a number"
    (should= (str default-invalid-input "Empty spaces are not a number\n")
             (not-a-valid-number "")))
  (it "explains that a letter is not a number"
    (should= (str default-invalid-input "'a' is not a number\n")
             (not-a-valid-number "a"))))

(describe "not-a-valid-move"
  (it "explains that number is out of range"
    (should= (str default-invalid-input
                  "There is no position 12 in the board\n")
             (not-a-valid-move 11)))
  (it "explains that a position is taken"
    (should= (str default-invalid-input
                  "The position is taken\n")
              (not-a-valid-move 2))))

(describe "invalid-marker-msg"
  (it "explains that a word is an invalid marker"
    (should= (str default-invalid-input "Marker must be a single letter.")
             (invalid-marker-msg "foo" "")))
  (it "explains that a number is not a valid marker"
    (should= (str default-invalid-input
                  "Numbers and special characters are not accepted.")
             (invalid-marker-msg "1" "")))
  (it "explains that a special character is not a valid input"
    (should= (str default-invalid-input
                  "Numbers and special characters are not accepted.")
             (invalid-marker-msg "#" "")))
  (it "explains when a marker is taken"
    (should= (str default-invalid-input
                  "This marker is taken by the first player.")
             (invalid-marker-msg "x" "x"))))
