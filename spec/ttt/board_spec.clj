(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]
            [clojure.set :as set]))

(describe "new-board"
  (it "returns a collection of empty spots"
    (should (every? #{empty-spot} (new-board))))

  (it "returns a collection with length equal to board-length"
    (should (= board-length (count (new-board))))))

(describe "board-rows"
  (it "returns a collection with size equals to board-size"
    (should (= board-size (count (board-rows)))))

  (it "returns a collection in which each element has size equals to board-size"
    (should (every? #(= board-size (count %)) (board-rows)))))

(describe "board-columns"
  (it "returns a collection with size equals to board-size"
    (should (= board-size (count (board-columns)))))

  (it "returns a collection in which each element has size equals to board-size"
    (should (every? #(= board-size (count %)) (board-columns)))))

(describe "diagonals"
  (context "forward"
    (it "returns indexes of diagonal forward"
      (should= [0 4 8] (diagonals 0 inc))))

  (context "backward"
    (it "retuns indexes of the diagonal backward"
      (should= [2 4 6] (diagonals (dec board-size) dec)))))

(describe "board-diagonals"
  (it "returns a collection that contains [0 4 8]"
    (should (some #(= [0 4 8] %) (board-diagonals))))

  (it "returns a collection that contains [6 7 8]"
    (should (some #(= [2 4 6] %) (board-diagonals))))

  (it "has only two elements"
    (should= 2 (count (board-diagonals)))))

(describe "winning-positions"
  (it "returns a collections with length 8"
    (should= 8 (count (winning-positions))))

  (it "returns a collection that contains board rows"
    (should (set/subset? #{[0 1 2] [3 4 5] [6 7 8]}
                         (set (winning-positions)))))

  (it "returns a collection that contains board columns"
    (should (set/subset? #{[0 3 6] [1 4 7] [2 5 8]}
                          (set (winning-positions)))))

  (it "returns a collection that contains board diagonals"
    (should (set/subset? #{[0 4 8] [2 4 6]} (set (winning-positions))))))

(describe "move"

  (with _ empty-spot)
  (with empty-board (new-board))

  (it "sets a value to an empty board"
    (should= [:x @_ @_ @_ @_ @_ @_ @_ @_]
             (move @empty-board 0 :x)))

  (it "sets a value in a given position after game has started"
    (should= [:x @_ @_ :o @_ :x @_ @_ @_]
             (move [:x @_ @_ :o @_ @_ @_ @_ @_] 5 :x))))

(describe "is-board-full?"

  (with _ empty-spot)

  (it "returns true if there is no empty spots"
    (should (is-board-full? (vec (repeat board-length :x)))))

  (it "returns false if there is any empty spot"
    (should-not (is-board-full? [:x :o :x :o @_ :x :x :o]))))

(describe "is-board-empty?"

  (with _ empty-spot)
  (with empty-board (new-board))

  (it "returns true if board has only empty spots"
    (should (is-board-empty? @empty-board)))

  (it "returns false if any spot has any marker"
    (should-not (is-board-empty? [@_ @_ @_ @_ @_ :x @_ @_ @_]))))

(describe "is-spot-available?"

  (with _ empty-spot)

  (it "returns true if spot is in range and board is empty"
    (should (is-spot-available? (new-board) 1)))

  (it "returns true if spot has an empty spot"
    (should (is-spot-available? [@_ @_ @_
                                 @_ :x :x
                                 :o :o @_] 1)))

  (it "returns false if spot has a marker"
    (should-not (is-spot-available? [@_ @_ @_
                                     @_ :x :x
                                     :o :o @_] 4))))

(describe "available-spots"

  (with _ empty-spot)
  (with full-board (vec (repeat board-length :x)))

  (it "returns a collection containing the index of the only empty spot"
    (should= '(8) (available-spots [:x :x :x :x :x :x :x :x @_])))

  (it "returns a collection containing the indexes of all empty spots"
    (should= '(0 1 8) (available-spots [@_ @_ :x :x :x :x :x :x @_])))

  (it "returns an empty collection if no empty spot"
    (should= '() (available-spots @full-board))))

(describe "is-valid-move?"

  (with _ empty-spot)
  (with empty-board (new-board))

  (it "returns true if spot is in range and board is empty"
    (should (is-valid-move? @empty-board 0)))

  (it "returns false if spot is bigger than board-length"
    (should-not (is-valid-move? @empty-board 10)))

  (it "returns true if spot is the index of one of the empty spots"
    (should (is-valid-move? [:x @_ :x @_ @_ @_ @_ @_ @_] 3)))

  (it "returns false if spot is taken"
    (should-not (is-valid-move? [@_ :x :x @_ @_ @_ @_ @_ @_] 1)))

  (it "returns false if spot is negative"
    (should-not (is-valid-move? @empty-board -10))))

(describe "repeated-markers?"

  (with _ empty-spot)
  (with empty-board (new-board))

  (it "returns false if board is empty"
    (should-not (repeated-markers? @empty-board [0 1 2])))

  (it "returns false if there is no repeated markers on given indexes"
    (should-not (repeated-markers? [:x :o :x
                                    :o :x :o
                                    :o :x :x] [0 1 2])))

  (it "returns true if there are repeated markers in a row"
    (should (repeated-markers? [:o :o :o
                                :x @_ @_
                                :x :x @_] [0 1 2])))

  (it "returns true if there are repeated markers in a diagonal"
    (should (repeated-markers? [:o @_ :x
                                :o :x @_
                                :x :o @_] [2 4 6])))

  (it "returns true if there are repeated markers in a column"
    (should (repeated-markers? [:o @_ :x
                                :o :x @_
                                :o :x @_] [0 3 6]))))

(describe "winning-combo"

  (with _ empty-spot)
  (with empty-board (new-board))

  (it "returns nil when board is empty"
    (should-not (winning-combo @empty-board)))

  (it "returns nil if finds a sequence of 3 empty spots and there is no winner"
    (should-not (winning-combo [:x :o :x
                                @_ @_ @_
                                :x @_ :o])))

  (it "returns winning combo even if there are repeated empty spots in winning positions"
    (should= [3 4 5] (winning-combo [@_ @_ @_
                                     :x :x :x
                                     :x @_ :o])))

  (it "returns winning row"
    (should= [0 1 2] (winning-combo [:x :x :x
                                     :o :o @_
                                     :o @_ @_])))

  (it "returns winning column"
    (should= [1 4 7] (winning-combo [:x :o :x
                                     @_ :o @_
                                     :x :o :x])))

  (it "returns winning diagonal"
    (should= [2 4 6] (winning-combo [:x @_ :o
                                     @_ :o :x
                                     :o :x :x]))))
