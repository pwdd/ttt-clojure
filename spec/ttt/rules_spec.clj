(ns ttt.rules-spec
  (:require [speclj.core :refer :all]
            [ttt.rules :as rules]
            [ttt.board :as board]))

(describe "find-empty-row"
  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})
  (with full-board (vec (repeat 9 @x)))

  (it "returns nil if there is not empty row"
    (should-not (rules/find-empty-row @full-board)))

  (it "returns one row that has only empty-spots"
    (should= [0 1 2] (rules/find-empty-row [@_ @_ @_
                                            @x @_ @_
                                            @_ @_ @o])))
  
  (it "returns the second row if it has only empty-spots"
    (should= [3 4 5] (rules/find-empty-row [@x @_ @_
                                            @_ @_ @_
                                            @o @_ @_])))
  
  (it "returns only one row if there are more rows empty"
    (should= [0 1 2] (rules/find-empty-row (board/new-board 3)))))

(describe "find-empty-column"
  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})
  (with full-board (vec (repeat 9 @x)))

  (it "returns nil if there is not empty column"
    (should-not (rules/find-empty-row @full-board)))

  (it "returns the first column if it is empty"
    (should= [0 3 6] (rules/find-empty-column [@_ @_ @x
                                               @_ @o @_
                                               @_ @_ @_])))
  
  (it "returns the second column if it is empty"
    (should= [1 4 7] (rules/find-empty-column [@_ @_ @x
                                               @o @_ @_
                                               @_ @_ @_]))))

(describe "find-empty-diagonal"
  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})
  (with full-board (vec (repeat 9 @x)))
  
  (it "returns nil if there is no empty diagonal"
    (should-not (rules/find-empty-diagonal @full-board)))
  
  (it "returns the forward diagonal if it is empty"
    (should= [0 4 8] (rules/find-empty-diagonal [@_ @x @_
                                                 @o @_ @_
                                                 @_ @_ @_])))
  
  (it "returns the backward diagonal if it is empty"
    (should= [2 4 6] (rules/find-empty-diagonal [@x @_ @_
                                                 @o @_ @_
                                                 @_ @_ @_]))))

(describe "place-in-the-middle"
  (it "returns the middle spot of a 3x3 board"
    (should= 4 (rules/place-in-the-middle (board/new-board 3))))
  
  (it "returns the middle spot for a 4x4 board"
    (should= 6 (rules/place-in-the-middle (board/new-board 4))))
  
  (it "returns the middle spot for a 5x5 board"
    (should= 12 (rules/place-in-the-middle (board/new-board 5)))))

(describe "place-in-the-corner"
  (with _ board/empty-spot)
  (with x {:token :x :color :blue})
  (with o {:token :o :color :green})

  (it "returns the first corner if it is empty in a 3x3 board"
    (should (some #{(rules/place-in-the-corner (board/new-board 3))}
                  [0 2 6 8])))
  
  (it "returns the only empty corner in a 3x3 board"
    (should= 2  (rules/place-in-the-corner [@x @_ @_
                                            @_ @_ @_
                                            @o @_ @x])))
  
  (it "returns an empty corner index in a 4x4 board"
    (should= 12 (rules/place-in-the-corner [@x @_ @_ @o
                                            @_ @_ @_ @_
                                            @_ @_ @_ @_
                                            @_ @_ @_ @_])))
  
  (it "returns an empty corner index in a 5x5 board"
    (should= 4 (rules/place-in-the-corner [@x @_ @_ @_ @_
                                           @_ @_ @_ @_ @_
                                           @_ @_ @_ @_ @_
                                           @_ @_ @_ @_ @_
                                           @_ @_ @_ @_ @_]))))


