(ns ttt.boards.board-spec
  (:require [speclj.core :refer :all]
            [clojure.set :as set]
            [ttt.boards.board :as board]))

(describe "new-board"
  (it "returns a collection of empty spots"
    (should (every? #{board/empty-spot} (board/new-board 3))))

  (it "returns a collection with length equal to board-length"
    (should (= (board/board-length 3) (count (board/new-board 3))))))

(describe "winning-positions"
  (it "returns a collections with length 8"
    (should= 8 (count (board/winning-positions 3))))

  (it "contains board-rows"
    (should (set/subset? (set (board/board-rows 3)) (set (board/winning-positions 3)))))

  (it "contains board-columns"
    (should (set/subset? (set (board/board-columns 3)) (set (board/winning-positions 3)))))

  (it "contains board-diagonals"
    (should (set/subset? (set (board/board-diagonals 3)) (set (board/winning-positions 3)))))

  (it "returns a collection in which each element has size equals to board-size"
    (should (every? #(= 3 (count %)) (board/winning-positions 3)))))

(describe "move"

  (with _ board/empty-spot)
  (with empty-board (board/new-board 3))
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

  (it "sets a value to an empty board"
    (should= [@x @_ @_ @_ @_ @_ @_ @_ @_]
             (board/move @empty-board 0 @x)))

  (it "sets a value in a given position after game has started"
    (should= [@x @_ @_ @o @_ @x @_ @_ @_]
             (board/move [@x @_ @_ @o @_ @_ @_ @_ @_] 5 @x))))

(describe "is-board-full?"

  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

  (it "returns true if there is no empty spots"
    (should (board/is-board-full? (vec (repeat (count @x) @x)))))

  (it "returns false if there is any empty spot"
    (should-not (board/is-board-full? [@x @o @x @o @_ @x @x @o]))))

(describe "is-board-empty?"

  (with _ board/empty-spot)
  (with empty-board (board/new-board 3))
  (with x {:token :x :color :green})

  (it "returns true if board has only empty spots"
    (should (board/is-board-empty? @empty-board)))

  (it "returns false if any spot has any marker"
    (should-not (board/is-board-empty? [@_ @_ @_ @_ @_ @x @_ @_ @_]))))

(describe "is-spot-available?"

  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

  (it "returns true if spot is in range and board is empty"
    (should (board/is-spot-available? (board/new-board 3) 1)))

  (it "returns true if spot has an empty spot"
    (should (board/is-spot-available? [@_ @_ @_
                                       @_ @x @x
                                       @o @o @_]
                                       1)))

  (it "returns false if spot has a marker"
    (should-not (board/is-spot-available? [@_ @_ @_
                                           @_ @x @x
                                           @o @o @_]
                                           4))))

(describe "available-spots"

  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

  (it "returns a collection containing the index of the only empty spot"
    (should= '(8) (board/available-spots [@x @x @x @x @x @x @x @x @_])))

  (it "returns a collection containing the indexes of all empty spots"
    (should= '(0 1 8) (board/available-spots [@_ @_ @x @x @x @x @x @x @_])))

  (it "returns an empty collection if no empty spot"
    (should= '() (board/available-spots (vec (repeat (count @x) @x))))))

(describe "is-valid-move?"

  (with _ board/empty-spot)
  (with empty-board (board/new-board 3))
  (with x {:token :x :color :green})

  (it "returns true if spot is in range and board is empty"
    (should (board/is-valid-move? @empty-board 0)))

  (it "returns false if spot is bigger than length of the board"
    (should-not (board/is-valid-move? @empty-board 10)))

  (it "returns true if spot is the index of one of the empty spots"
    (should (board/is-valid-move? [@x @_ @x @_ @_ @_ @_ @_ @_] 3)))

  (it "returns false if spot is taken"
    (should-not (board/is-valid-move? [@_ @x @x @_ @_ @_ @_ @_ @_] 1)))

  (it "returns false if spot is negative"
    (should-not (board/is-valid-move? @empty-board -10))))

(describe "repeated-markers?"

  (with _ board/empty-spot)
  (with empty-board (board/new-board 3))
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

  (it "returns false if board is empty"
    (should-not (board/repeated-markers? @empty-board [0 1 2])))

  (it "returns false if there is no repeated markers on given indexes"
    (should-not (board/repeated-markers? [@x @o @x
                                          @o @x @o
                                          @o @x @x]
                                          [0 1 2])))

  (it "returns true if there are repeated markers in a row"
    (should (board/repeated-markers? [@o @o @o
                                      @x @_ @_
                                      @x @x @_]
                                      [0 1 2])))

  (it "returns true if there are repeated markers in a diagonal"
    (should (board/repeated-markers? [@o @_ @x
                                      @o @x @_
                                      @x @o @_]
                                      [2 4 6])))

  (it "returns true if there are repeated markers in a column"
    (should (board/repeated-markers? [@o @_ @x
                                      @o @x @_
                                      @o @x @_]
                                      [0 3 6]))))

(describe "winning-combo"

  (with _ board/empty-spot)
  (with empty-board (board/new-board 3))
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

  (it "returns nil when board is empty"
    (should-not (board/winning-combo @empty-board)))

  (it "returns nil if finds a sequence of 3 empty spots and there is no winner"
    (should-not (board/winning-combo [@x @o @x
                                      @_ @_ @_
                                      @x @_ @o])))

  (it "returns winning combo even if there are repeated empty spots in winning positions"
    (should= [3 4 5] (board/winning-combo [@_ @_ @_
                                           @x @x @x
                                           @x @_ @o])))

  (it "returns winning row"
    (should= [0 1 2] (board/winning-combo [@x @x @x
                                           @o @o @_
                                           @o @_ @_])))

  (it "returns winning column"
    (should= [1 4 7] (board/winning-combo [@x @o @x
                                           @_ @o @_
                                           @x @o @x])))

  (it "returns winning diagonal"
    (should= [2 4 6] (board/winning-combo [@x @_ @o
                                           @_ @o @x
                                           @o @x @x]))))
