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

(describe "is-valid-move?"
  (it "returns true if input is valid move"
    (should (is-valid-move? [:_ :_ :_ :_ :_ :_ :_ :_ :_] 0)))
  (it "returns true if is valid move on a board with spots taken"
    (should (is-valid-move? [:x :_ :o :_ :_ :_ :_ :_ :_] 3)))
  (it "returns false if move is not valid"
    (should-not (is-valid-move? [:_ :x :o :_ :_ :_ :_ :_ :_] 1))))

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

(describe "draw?"
  (it "returns false if board is empty"
    (should-not (draw? [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns false if board is not full"
    (should-not (draw? [:x :x :x :x :x :x :x :x :_])))
  (it "returns false if there is a winner"
    (should-not (draw? [:o :x :_
                        :o :_ :x
                        :o :x :x])))
  (it "returns false if board is full and there is a winner"
    (should-not (draw? [:x :o :x
                        :o :x :o
                        :o :o :x])))
  (it "returns true if board is full and there is no winner"
    (should (draw? [:x :o :x
                    :o :x :o
                    :o :x :o]))))

(describe "game-over?"
  (it "returns false if board is empty"
    (should-not (game-over? [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns false if only some spots are taken"
    (should-not (game-over? [:x :o :_ :_ :_ :x :_ :_ :o])))
  (it "returns true if there is a draw"
    (should (game-over? [:x :o :x
                         :o :x :o
                         :o :x :o])))
  (it "returns true if there is a winner"
    (should (game-over? [:x :x :x
                         :o :_ :o
                         :o :x :o]))))

(describe "winning-combo"
  (it "returns nothing when board is empty"
    (should-not (winning-combo [:_ :_ :_
                                :_ :_ :_
                                :_ :_ :_])))
  (it "returns nothing if finds 3 empty spots"
    (should-not (winning-combo [:x :o :x
                                :_ :_ :_
                                :x :_ :o])))
  (it "returns winning row"
    (should= [0 1 2] (winning-combo [:x :x :x
                                     :o :o :_
                                     :o :_ :_])))
  (it "returns winning column"
    (should= [1 4 7] (winning-combo [:x :o :x
                                     :_ :o :_
                                     :x :o :x])))
  (it "returns winning diagonal"
    (should= [2 4 6] (winning-combo [:x :_ :o
                                     :_ :o :x
                                     :o :x :x]))))
