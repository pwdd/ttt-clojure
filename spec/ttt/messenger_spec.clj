(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :as messenger]
            [ttt.player :as player]
            [ttt.game :as game]
            [ttt.board :as board]
            [ttt.helpers :as helpers]))

(describe "separator"
  (it "returns the board row separation for a 3 x 3 board"
    (should= "\n---|---|---\n"
             (helpers/remove-color (messenger/separator 3))))

  (it "returns the board row separation for a 4 x 4 board"
    (should= "\n---|---|---|---\n"
             (helpers/remove-color (messenger/separator 4))))

  (it "returns the board row separation for a 5 x 5 board"
    (should= "\n---|---|---|---|---\n"
             (helpers/remove-color (messenger/separator 5)))))

(describe "ask-role-msg"
  (with x {:token :x :color :blue})

  (it "returns a message that contains a player's marker"
    (should (re-find #" 'x' "
                     (helpers/remove-color (messenger/ask-role-msg @x))))))

(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x "
             (helpers/remove-color (messenger/translate-keyword [1 
                                                                {:token :x
                                                                 :color :blue}]))))

  (it "returns ' o '"
    (should= " o "
             (helpers/remove-color (messenger/translate-keyword [1
                                                                {:token :o
                                                                 :color :blue}]))))

  (it "returns a number surrounded by whitespaces if index of empty spot is smaller than 9"
    (should= " 2 " (helpers/remove-color (messenger/translate-keyword [1 board/empty-spot]))))
  
  (it "returns whitespace and number if empty spot index is bigger than 9"
    (should= " 10" (helpers/remove-color (messenger/translate-keyword [9 board/empty-spot])))))

(describe "stringify-board"

  (with x {:token :x :color :reset})
  (with o {:token :o :color :reset})
  (with _ board/empty-spot)

  (it "outputs a representation of the empty board with dimension 3x3"
    (should= (str   " 1 | 2 | 3 "
                  "\n---|---|---\n"
                    " 4 | 5 | 6 "
                  "\n---|---|---\n"
                    " 7 | 8 | 9 \n")
      (helpers/remove-color (messenger/stringify-board (board/new-board 3)))))

  (it "outputs a representation of the empty board with dimension 4x4"
    (should= (str   " 1 | 2 | 3 | 4 "
                  "\n---|---|---|---\n"
                    " 5 | 6 | 7 | 8 "
                  "\n---|---|---|---\n"
                    " 9 | 10| 11| 12"
                  "\n---|---|---|---\n"
                    " 13| 14| 15| 16\n")
      (helpers/remove-color (messenger/stringify-board (board/new-board 4)))))

  (it "combines empty spaces and letters when some spots are taken"
    (should= (str   " 1 | x | 3 "
                  "\n---|---|---\n"
                    " o | 5 | 6 "
                  "\n---|---|---\n"
                    " 7 | 8 | x \n")
             (helpers/remove-color (messenger/stringify-board [@_ @x @_ @o @_ @_ @_ @_ @x])))))

(describe "stringify-combo"
  (it "returns a string representing a vector of numbers"
    (should= "1, 2, 3" (messenger/stringify-combo [0 1 2])))

  (it "increases number by one"
    (should= "3, 5, 7" (messenger/stringify-combo [2 4 6]))))

(describe "result"
  (context ":default"

    (with game (game/create-game :human :human))
    (with x {:token :x :color :green})
    (with o {:token :o :color :blue})
    (with _ board/empty-spot)
    (with first-player {:role :human :marker @x})
    (with second-player {:role :human :marker @o})

    (it "returns a message saying that the game ends ties"
      (should= "The game tied" (messenger/result @game
                                                 [@x @o @x
                                                  @o @x @o
                                                  @o @x @o])))

    (it "returns a message that has the marker 'x' and the winning positions"
      (should (re-find #"(?=^Player )(?=.*x.*)(?=.*1, 2, 3.*)"
               (messenger/result @game
                                 [@x @x @x
                                  @o @_ @o
                                  @o @x @o]
                                  @first-player
                                  @second-player))))

    (it "returns a message that has the marker 'o' and the winning positions"
      (should (re-find #"(?=^Player )(?=.*o.*)(?=.*1, 5, 9.*)"
               (messenger/result @game
                                 [@o @x @x
                                  @x @o @_
                                  @_ @_ @o]
                                  @first-player
                                  @second-player)))))

  (context ":computer-x-human"

    (with x {:token :x :color :green})
    (with o {:token :o :color :blue})
    (with _ board/empty-spot)
    (with game (game/create-game :human :easy-computer))
    (with human {:marker @x :role :human})
    (with easy-computer {:marker @o :role :easy-computer})

    (it "returns tied message if the game ties"
      (should= "You tied\n" (messenger/result @game
                                              [@x @o @x
                                               @o @x @o
                                               @o @x @o]
                                               @human
                                               @easy-computer)))

    (it "returns winning message if human player won"
      (should (re-find #"You won(.*)"
                       (messenger/result @game
                                         [@x @x @x
                                          @o @_ @o
                                          @o @x @o]
                                          @human
                                          @easy-computer))))

    (it "returns 'you lost' message if human player lost"
      (should (re-find #"You lost(.*)"
                       (messenger/result @game
                                         [@o @x @x
                                          @x @o @_
                                          @_ @_ @o]
                                          @human
                                          @easy-computer))))))

(describe "moved-to"

  (with human {:marker {:token :x :color :blue} :role :human})
  (with easy-computer {:marker {:token :o :color :blue} :role :easy-computer})
  (with hard-computer {:marker {:token :h :color :blue} :role :hard-computer})
  (with human-x-human (game/create-game :human :human))
  (with easy-x-human (game/create-game :easy-computer :human))
  (with easy-x-easy (game/create-game :easy-computer :easy-computer))
  (with hard-x-human (game/create-game :hard-computer :human))
  (with hard-x-hard (game/create-game :hard-computer :hard-computer))

  (context ":computer-x-human"
    (it "returns a message starting with 'You' if player is human"
      (should= "You moved to 2\n"
              (messenger/moved-to @easy-x-human @human 1)))

    (it "returns a message starting with 'Easy-computer' if player is easy-computer"
      (should= "Easy-computer moved to 2\n"
               (messenger/moved-to @easy-x-human @easy-computer 1))))

  (context ":same-player-roles"
    (it "returns message starting with 'Player [marker]' if player is human"
      (should (re-find #"(?=^Player )(?=.*x.*)(?=.*2.*)"
              (messenger/moved-to @human-x-human @human 1)))))

    (it "returns message starting with 'Player [marker]' if player is easy-computer"
      (should (re-find #"(?=^Player )(?=.*o.*)(?=.*3.*)"
              (messenger/moved-to @easy-x-easy @easy-computer 2))))

    (it "returns message starting with 'Player [marker]' if player is hard-computer"
      (should (re-find #"(?=^Player )(?=.*h.*)(?=.*9.*)"
              (messenger/moved-to @hard-x-hard @hard-computer 8)))))

(describe "not-a-valid-number"
  (it "explains that empty space is not a number"
    (should= (str messenger/default-invalid-input "Empty spaces are not a number\n")
             (messenger/not-a-valid-number "")))

  (it "explains that a letter is not a number"
    (should= (str messenger/default-invalid-input "'a' is not a number\n")
             (messenger/not-a-valid-number "a"))))

(describe "not-a-valid-move"
  (it "explains that number is out of range"
    (should= (str messenger/default-invalid-input "There is no position 12 in the board\n")
             (messenger/not-a-valid-move 11 9)))

  (it "explains that a position is taken"
    (should= (str messenger/default-invalid-input "The position is taken\n")
             (messenger/not-a-valid-move 2 16))))

(describe "invalid-marker-msg"
  (it "explains that a word is an invalid marker"
    (should= (str messenger/default-invalid-input "Marker must be a single letter.")
             (messenger/invalid-marker-msg "foo" "")))

  (it "explains that a number is not a valid marker"
    (should= (str messenger/default-invalid-input
                  "Numbers and special characters are not accepted.")
             (messenger/invalid-marker-msg "1" "")))

  (it "explains that a special character is not a valid input"
    (should= (str messenger/default-invalid-input
                  "Numbers and special characters are not accepted.")
             (messenger/invalid-marker-msg "#" "")))

  (it "explains when a marker is taken"
    (should= (str messenger/default-invalid-input
                  "This marker is taken by the first player.")
             (messenger/invalid-marker-msg "x" "x"))))

(describe "current-player-is"
  (it "returns a string containing 'x'"
    (should (re-find #"'x'"
                    (helpers/remove-color
                     (messenger/current-player-is {:token :x :color :blue})))))

  (it "returns a string containing 'a'"
    (should (re-find #"'a'"
                     (helpers/remove-color
                      (messenger/current-player-is {:token :a :color :blue}))))))

(describe "display-files-list"
  (it "returns an empty string if collection is empty"
    (should= "" (messenger/display-files-list [])))

  (it "returns the string of the only element in a collection"
    (should= "abc" (messenger/display-files-list ["abc"])))

  (it "returns a string with all the elements from a collection"
    (should= "abc, def, foo" (messenger/display-files-list ["abc" "def" "foo"]))))
