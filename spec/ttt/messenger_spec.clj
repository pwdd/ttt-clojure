(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :refer :all]
            [ttt.player :refer [make-player]]
            [ttt.game :refer [create-game]]))

(def human (make-player { :marker :x :role :human }))
(def computer (make-player { :marker :o :role :easy-computer }))
(def game (create-game human human))

(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x " (translate-keyword :x)))
  (it "returns ' o '"
    (should= " o " (translate-keyword :o)))
  (it "returns an empty space for :_"
    (should= "   " (translate-keyword :_))))

(describe "stringfy-board"
  (it "outputs a numbered representation of the board if no spot is taken"
    (should=
      "   |   |   \n---|---|---\n   |   |   \n---|---|---\n   |   |   \n"
      (stringfy-board [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "combines numbers and letters when some spots are taken"
    (should=
      "   | x |   \n---|---|---\n o |   |   \n---|---|---\n   |   | x \n"
      (stringfy-board [:_ :x :_ :o :_ :_ :_ :_ :x]))))

(describe "separator"
  (it "has a default value"
    (should= "\n---|---|---\n" separator)))

(describe "stringfy-combo"
  (it "returns 1, 2, 3"
    (should= "1, 2, 3" (stringfy-combo [0 1 2])))
  (it "returns 3, 5, 7"
    (should= "3, 5, 7" (stringfy-combo [2 4 6]))))

(describe "result-human-computer"
  (it "returns tied message if the game ties"
    (should= "You tied\n" (result-human-computer [:x :o :x
                                                  :o :x :o
                                                  :o :x :o]
                                                  human
                                                  computer)))
  (it "returns winning message if human player won"
    (should (re-find #"You won\W*(.*)"
                     (result-human-computer [:x :x :x
                                             :o :_ :o
                                             :o :x :o]
                                             human
                                             computer))))

  (it "returns 'you lost' message if human player lost"
    (should (re-find #"You lost\W*(.*)" (result-human-computer [:o :x :x
                                                 :x :o :_
                                                 :_ :_ :o]
                                                 human
                                                 computer)))))

(describe "result"
 (it "returns 'tie' the game ends ties"
   (should= "The game tied" (result game
                                    [:x :o :x
                                     :o :x :o
                                     :o :x :o])))
 (it "returns X if first player won"
   (should= "Player X won on positions 1, 2, 3" (result game
                                                        [:x :x :x
                                                         :o :_ :o
                                                         :o :x :o])))
 (it "returns 'O' if second player won"
   (should= "Player O won on positions 1, 5, 9" (result game
                                                        [:o :x :x
                                                         :x :o :_
                                                         :_ :_ :o]))))

(describe "moved-to"
  (it "returns empty string if player is human"
    (should (empty? (moved-to human 1))))
  (it "returns a message to where computer moved incremented by one"
    (should-not (empty? (moved-to computer 3)))))

(describe "print-message"
  (around [it]
    (with-out-str (it)))
  (it "outputs a message to stdout"
    (should= "test message\r\n" (with-out-str (print-message "test message")))))

(describe "not-a-number"
  (it "explains that empty space is not a number"
    (should= "\nYour choice is not valid. Empty spaces are not a number"
             (not-a-number "")))
  (it "builds a string explaining that input is not a number"
    (should= "\nYour choice is not valid. 'a' is not a number"
             (not-a-number "a"))))
