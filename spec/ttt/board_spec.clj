(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]))

(describe "board-size"
  (it "has a default size"
    (should= 3 board-size)))

(describe "board-length"
  (it "has the default value of squared board-size"
    (should= (* board-size board-size) board-length)))

(describe "winning-combos"
  (it "holds all winning combination"
    (should= 8 (count winning-combos))))

(describe "new-board"
  (it "is a vector of empty spots"
    (should (every? #{empty-spot} (new-board))))
  (it "has length equal to board-length"
    (should (= board-length (count (new-board))))))

(describe "move"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "sets a value to an empty board"
      (should= [:x _ _ _ _ _ _ _ _]
               (move empty-board :x 0)))
    (it "sets a spot on a board with some spots already taken"
      (should= [:x _ _ :o _ :x _ _ _]
               (move [:x _ _ :o _ _ _ _ _] :x 5)))))

(describe "is-spot-available?"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns true when spot is not taken"
        (should (is-spot-available? empty-board 0)))
     (it "returns false when spot is taken"
        (should-not (is-spot-available? [:x _ _ _ _ _ _ _ _] 0)))))

(describe "is-board-full?"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns false if board is empty"
      (should-not (is-board-full? empty-board)))
    (it "returns true if board is full"
      (should (is-board-full? (vec (repeat board-length :x)))))
    (it "returns false if there is any spot available"
      (should-not (is-board-full? [:x :o :x :o _ :x :x :o])))))

(describe "is-board-empty?"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns true if board has only empty spots"
      (should (is-board-empty? empty-board)))
    (it "returns false if any spot is taken"
      (should-not (is-board-empty? [_ _ _ _ _ :x _ _ _])))))

(describe "available-spots"
  (let [_ empty-spot
        full-board (vec (repeat board-length :x))]
    (it "returns a collection containing the index of the only available spot"
      (should= '(8) (available-spots [:x :x :x :x :x :x :x :x _])))
    (it "returns a collection containing the indexes of all available spots"
      (should= '(0 1 8) (available-spots [_ _ :x :x :x :x :x :x _])))
    (it "returns an empty collection if no spot is available"
      (should= '() (available-spots full-board)))))

(describe "is-valid-move?"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns true if argument is in range when board is empty"
      (should (is-valid-move? empty-board 0)))
    (it "returns true if argument is the index of one of the empty spots"
      (should (is-valid-move? [:x _ :x _ _ _ _ _ _] 3)))
    (it "returns false if spot is taken"
      (should-not (is-valid-move? [_ :x :x _ _ _ _ _ _] 1)))
    (it "returns false if number is bigger than board-length"
      (should-not (is-valid-move? empty-board 10)))
    (it "returns false if number is negative"
      (should-not (is-valid-move? empty-board -10)))))

(describe "repeated?"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns false if board is empty"
      (should-not (repeated? empty-board [0 1 2])))
    (it "returns false if there is no repetition on given indexes"
      (should-not (repeated? [:x :o :x
                              :o :x :o
                              :o :x :x] [0 1 2])))
    (it "returns true if there are repeated elements in a row"
      (should (repeated? [:o :o :o
                          :x  _  _
                          :x :x  _] [0 1 2])))
    (it "returns true if there are repeated elements in a diagonal"
      (should (repeated? [:o _ :x
                          :o :x _
                          :x :o _] [2 4 6])))
    (it "returns true if there are repeated elements in a column"
      (should (repeated? [:o  _  :x
                          :o  :x  _
                          :o  :x  _] [0 3 6])))))

(describe "find-repetition"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns an empty collection if board is empty"
      (should= '()
               (find-repetition empty-board)))
    (it "returns an empty collection if board is full and there is no winner"
      (should= '()
               (find-repetition [:x :o :x
                                 :o :x :o
                                 :o :x :o])))
    (it "identifies a single combo with indexes of repeated markers"
      (should= '([0 1 2])
               (find-repetition [:x :x :x
                                 :o _ _
                                 :o :o _])))
    (it "identifies multiple combos with indexes of repeated markers"
      (should= '([3 4 5] [6 7 8])
                (find-repetition [:x :o _
                                  :o :o :o
                                  :x :x :x])))))

(describe "winning-combo"
  (let [_ empty-spot
        empty-board (new-board)]
    (it "returns nil when board is empty"
      (should-not (winning-combo empty-board)))
    (it "returns nil if finds a sequence of 3 empty spots and there is no winner"
      (should-not (winning-combo [:x :o :x
                                  _   _  _
                                  :x  _  :o])))
    (it "returns winning combo even if there are repeated empty spots in winning positions"
      (should= [3 4 5] (winning-combo [_  _  _
                                       :x :x :x
                                       :x  _ :o])))
    (it "returns winning row"
      (should= [0 1 2] (winning-combo [:x :x :x
                                       :o :o  _
                                       :o _ _])))
    (it "returns winning column"
      (should= [1 4 7] (winning-combo [:x :o :x
                                       _  :o  _
                                       :x :o :x])))
    (it "returns winning diagonal"
      (should= [2 4 6] (winning-combo [:x _  :o
                                       _  :o :x
                                       :o :x :x])))))
