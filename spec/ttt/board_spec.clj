(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]))

(describe "board-size"
  (it "has a default size"
    (should= 3 board-size)))

(describe "board-length"
  (it "has the default value of squared board-size"
    (should= 9 board-length)))

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

(describe "get-rows"
  (it "gets all rows from board if board is empty"
    (should= [[:_ :_ :_] [:_ :_ :_] [:_ :_ :_]]
             (get-rows [:_ :_ :_
                        :_ :_ :_
                        :_ :_ :_])))
  (it "gets rows if spots are taken"
    (should= [[:x :o :_] [:_ :x :x] [:_ :o :o]]
             (get-rows [:x :o :_
                        :_ :x :x
                        :_ :o :o]))))

(describe "get-cols"
  (it "gets columns from empty board"
    (should= [[:_ :_ :_] [:_ :_ :_] [:_ :_ :_]]
             (get-cols [:_ :_ :_
                        :_ :_ :_
                        :_ :_ :_])))
  (it "gets columns if spots are taken"
    (should= [[:x :o :x] [:o :x :x] [:o :x :x]]
             (get-cols [:x :o :o
                        :o :x :x
                        :x :x :x]))))

(describe "get-diagonals"
  (it "gets diagonals from empty board"
    (should= [[:_ :_ :_] [:_ :_ :_]]
             (get-diagonals [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "gets diagonals on board with spots taken"
    (should= [[:x :x :x] [:o :x :x]]
             (get-diagonals [:x :o :o
                             :o :x :x
                             :x :x :x]))))

(describe "get-combos"
  (it "get cols, rows and diagonals from empty board"
    (should= [[:_ :_ :_] [:_ :_ :_] [:_ :_ :_]
              [:_ :_ :_] [:_ :_ :_] [:_ :_ :_]
              [:_ :_ :_] [:_ :_ :_]]
              (get-combos [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "get cols, rows and diagonals from empty board"
    (should= [[:x :o :o] [:o :x :x] [:x :x :x]
              [:x :o :x] [:o :x :x] [:o :x :x]
              [:x :x :x] [:o :x :x]]
              (get-combos [:x :o :o
                           :o :x :x
                           :x :x :x]))))

(describe "winner"
  (it "returns nil if board is empty"
    (should (nil? (winner [:_ :_ :_ :_ :_ :_ :_ :_ :_]))))
  (it "returns nil if there is no winner"
    (should (nil? (winner [:x :o :x
                           :o :x :o
                           :o :x :o]))))
  (it "returns the winner if there is one on rows"
    (should= :x (winner [:x :x :x
                         :_ :_ :o
                         :o :o :_])))
  (it "returns the winner if there is one in second row"
    (should= :x (winner [:_ :_ :o
                         :x :x :x
                         :o :o :_])))
  (it "returns the winner if there is one in column"
    (should= :x (winner [:x :_ :_
                         :x :o :o
                         :x :_ :_])))
  (it "returns the winner if there is one in the second row"
    (should= :x (winner [:_ :x :_
                         :o :x :o
                         :o :x :_])))
  (it "returns the winner if there is one in a diagonal"
    (should= :o (winner [:o :x :x
                         :x :o :_
                         :_ :_ :o])))
  (it "returns the winner if there is one in the other diagonal"
    (should= :x (winner [:x :_ :x
                         :o :x :o
                         :x :o :_]))))


; (describe "winner-two"
;   (it "returns nil if board is empty"
;     (should (nil? (winner-two [:_ :_ :_ :_ :_ :_ :_ :_ :_]))))
;   (it "returns nil if there is no winner"
;     (should (nil? (winner-two [:x :o :x
;                                :o :x :o
;                                :o :x :o]))))
;   (it "returns the winner if there is one on rows"
;     (should= :x (winner-two [:x :x :x
;                              :_ :_ :o
;                              :o :o :_])))
;   (it "returns the winner if there is one in second row"
;     (should= :x (winner-two [:_ :_ :o
;                              :x :x :x
;                              :o :o :_])))
;   (it "returns the winner if there is one in column"
;     (should= :x (winner-two [:x :_ :_
;                              :x :o :o
;                              :x :_ :_])))
;   (it "returns the winner if there is one in the second row"
;     (should= :x (winner-two [:_ :x :_
;                              :o :x :o
;                              :o :x :_])))
;   (it "returns the winner if there is one in a diagonal"
;     (should= :o (winner-two [:o :x :x
;                              :x :o :_
;                              :_ :_ :o])))
;   (it "returns the winner if there is one in the other diagonal"
;     (should= :x (winner-two [:x :_ :x
;                              :o :x :o
;                              :x :o :_]))))
