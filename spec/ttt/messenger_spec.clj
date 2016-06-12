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
