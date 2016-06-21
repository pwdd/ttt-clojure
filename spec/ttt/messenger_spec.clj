(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :refer :all]))

(describe "print-board"
  (it "outputs a numbered representation of the board if no spot is taken"
    (should=
      "   |   |   \n---|---|---\n   |   |   \n---|---|---\n   |   |   \n"
      (print-board [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "combines numbers and letters when some spots are taken"
    (should=
      "   | x |   \n---|---|---\n o |   |   \n---|---|---\n   |   | x \n"
      (print-board [:_ :x :_ :o :_ :_ :_ :_ :x]))))

(describe "separator"
  (it "has a default value"
    (should= "\n---|---|---\n" separator)))

(describe "print-combo"
  (it "returns 1, 2, 3"
    (should= "1, 2, 3" (print-combo [0 1 2])))
  (it "returns 3, 5, 7"
    (should= "3, 5, 7" (print-combo [2 4 6]))))

(describe "result-human-computer"
  (it "returns tied message the game ends ties"
    (should= "You tied\n" (result-human-computer [:x :o :x
                                   :o :x :o
                                   :o :x :o]
                                   {:type "human" :marker :x}
                                   {:type "computer" :marker :o})))
  (it "returns winning message human player won"
    (should (re-find #"You won\W*(.*)"  (result-human-computer [:x :x :x
                                                                :o :_ :o
                                                                :o :x :o]
                                                                {:type "human" :marker :x}
                                                                {:type "computer" :marker :o}))))

  (it "returns 'you lost' message if human player lost"
    (should (re-find #"You lost\W*(.*)" (result-human-computer [:o :x :x
                                                 :x :o :_
                                                 :_ :_ :o]
                                                 {:type "human" :marker :x}
                                                 {:type "computer" :marker :o})))))

(describe "result"
 (it "returns 'tie' the game ends ties"
   (should= "The game tied" (result [:x :o :x
                                     :o :x :o
                                     :o :x :o])))
 (it "returns X if first player won"
   (should= "Player X won on positions 1, 2, 3" (result [:x :x :x
                                                         :o :_ :o
                                                         :o :x :o])))
 (it "returns 'O' if second player won"
   (should= "Player O won on positions 1, 5, 9" (result [:o :x :x
                                                         :x :o :_
                                                         :_ :_ :o]))))

(describe "moved-to"
  (it "returns empty string if player is human"
    (should (empty? (moved-to {:type "human" :marker :x} 1))))
  (it "returns a message to where computer moved incremented by one"
    (should= "Computer moved to 4" (moved-to {:type "computer"
                                              :marker :o}
                                              3))))
