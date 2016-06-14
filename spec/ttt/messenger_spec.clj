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

(describe "result"
  (it "returns 'tie' the game ends ties"
    (should= "tie" (result [:x :o :x
                            :o :x :o
                            :o :x :o])))
  (it "returns X if first player won"
    (should= "Player X won on positions [1 2 3]" (result [:x :x :x
                                    :o :_ :o
                                    :o :x :o])))
  (it "returns 'O' if second player won"
    (should= "Player O won on positions [1 5 9]" (result [:o :x :x
                                                          :x :o :_
                                                           :_ :_ :o]))))

(describe "print-combo"
  (it "returns [1 2 3]"
    (should= [1 2 3] (print-combo [0 1 2])))
  (it "returns [3 5 7]"
    (should= [3 5 7] (print-combo [2 4 6]))))
