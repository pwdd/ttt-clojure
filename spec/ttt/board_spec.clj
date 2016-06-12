(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]))

(describe "board-size"
  (it "has a default size"
    (should= 3 board-size)))

(describe "board-length"
  (it "has the default value of squared board-size"
    (should= 9 board-length)))

(describe "winning-positions"
  (it "holds a vector with all winning positions"
    (should= 8 (count winning-positions))))

(describe "new-board"
  (it "is a vector of (range 9)"
    (should= [:_ :_ :_ :_ :_ :_ :_ :_ :_]
             (new-board))))

(describe "is-empty-spot?"
  (it "returns true if spot is :_"
    (should (is-empty-spot? :_))
  (it "returns false otherwise"
    (should-not (is-empty-spot? :x)))))

(describe "move"
  (it "sets a value to an empty board"
    (should= [:x :_ :_ :_ :_ :_ :_ :_ :_]
             (move [:_ :_ :_ :_ :_ :_ :_ :_ :_] :x 0)))
  (it "sets a spot on a board with some spots already taken"
    (should= [:x :_ :_ :o :_ :x :_ :_ :_]
             (move [:x :_ :_ :o :_ :_ :_ :_ :_] :x 5))))

(describe "is-available?"
  (it "returns true when spot is not taken"
      (should (is-available? [:_ :_ :_ :_ :_ :_ :_ :_ :_] 0)))
   (it "returns false when spot is taken"
      (should-not (is-available? [:x :_ :_ :_ :_ :_ :_ :_ :_] 0))))

(describe "is-full?"
  (it "returns true if board is full"
    (should (is-full? [:x :x :x :x :x :x :x :x :x])))
  (it "returns false if there is any spot available"
    (should-not (is-full? [:x :x :x :x :x :x :x :x :_]))))

(describe "available-spots"
  (it "returns a list with one element if only one spot is available"
    (should= '(8) (available-spots [:x :x :x :x :x :x :x :x :_])))
  (it "returns a vector with more than one spot available"
    (should= '(0 1 8) (available-spots [:_ :_ :x :x :x :x :x :x :_])))
  (it "returns an empty vector is no spot is available"
    (should= '() (available-spots [[:x :x :x :x :x :x :x :x :x]]))))

(describe "in-range?"
  (it "returns true if number is in range"
    (should (in-range? 2)))
  (it "returns false if number is not in range"
    (should-not (in-range? 10)))
  (it "returns true if 0"
      (should (in-range? 0)))
  (it "returns true if 9"
      (should (in-range? 9))))

(describe "is-valid-move?"
  (it "returns true if input is valid move"
    (should (is-valid-move? [:_ :_ :_ :_ :_ :_ :_ :_ :_] 0)))
  (it "returns true if is valid move on a board with spots taken"
    (should (is-valid-move? [:x :_ :o :_ :_ :_ :_ :_ :_] 3)))
  (it "returns false if move is not valid"
    (should-not (is-valid-move? [:_ :x :o :_ :_ :_ :_ :_ :_] 1))))
