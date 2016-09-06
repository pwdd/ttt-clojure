(ns ttt.rules-spec
  (:require [speclj.core :refer :all]
            [ttt.rules :as rules]
            [ttt.board :as board]
            [clojure.set :as set]))

(describe "find-empty-combos"
  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})
  (with full-board (vec (repeat 9 @x)))
  (with combos (board/winning-positions 3))

  (it "returns [] if there is not an empty combo"
    (should= [] (rules/find-empty-combos @full-board @combos)))

  (it "returns a collection with one combo that has only empty-spots"
    (should= [[0 1 2]] (rules/find-empty-combos [@_ @_ @_
                                                 @x @_ @x
                                                 @x @o @o]
                                                 @combos)))

  (it "returns a collection with combos that have only empty-spots"
    (let [solution (rules/find-empty-combos [@x @_ @_
                                             @_ @_ @_
                                             @o @_ @_]
                                             @combos)
          solution-map (frequencies solution)
          expected-map (frequencies [[3 4 5] [2 5 8] [1 4 7]])]
      (should= expected-map solution-map))))

(describe "place-in-the-center"
  (it "returns the middle spot of a 3x3 board"
    (should= 4 (rules/place-in-the-center (board/new-board 3))))

  (it "returns the middle spot for a 4x4 board"
    (should= 6 (rules/place-in-the-center (board/new-board 4))))

  (it "returns the middle spot for a 5x5 board"
    (should= 12 (rules/place-in-the-center (board/new-board 5)))))

(describe "place-in-a-corner"
  (with _ board/empty-spot)
  (with x {:token :x :color :blue})
  (with o {:token :o :color :green})

  (it "returns the a corner if it is empty in a 3x3 board"
    (let [solution (rules/place-in-a-corner (board/new-board 3))]
      (should (some #{solution} [0 2 6 8]))))

  (it "returns the only empty corner in a 3x3 board"
    (let [solution (rules/place-in-a-corner [@x @_ @_
                                             @_ @_ @_
                                             @o @_ @x])]
      (should= 2 solution)))

  (it "returns an empty corner index in a 4x4 board"
    (let [solution (rules/place-in-a-corner [@x @_ @_ @o
                                             @_ @_ @_ @_
                                             @_ @_ @_ @_
                                             @_ @_ @_ @_])]
      (should (some #{solution} [12 15]))))

  (it "returns an empty corner index in a 5x5 board"
    (let [solution (rules/place-in-a-corner [@x @_ @_ @_ @_
                                             @_ @_ @_ @_ @_
                                             @_ @_ @_ @_ @_
                                             @_ @_ @_ @_ @_
                                             @_ @_ @_ @_ @_])]
      (should (some #{solution} [4 20 24])))))

(describe "is-center-the-best-move?"
  (with _ board/empty-spot)
  (with o {:token :o :color :blue})
  (with x {:token :x :color :green})

  (it "returns true if board is empty"
    (should (rules/is-center-the-best-move? (board/new-board 3))))

  (it "returns true if opponent has made only one move and not in the middle"
    (should (rules/is-center-the-best-move? [@o @_ @_
                                             @_ @_ @_
                                             @_ @_ @_])))

  (it "returns false if board has more than one move"
    (should-not (rules/is-center-the-best-move? [@o @o @_
                                                 @x @_ @_
                                                 @_ @_ @_]))))

(describe "available-spots-in-combo"
  (with _ board/empty-spot)
  (with o {:token :o :color :blue})
  (with x {:token :x :color :green})
  (with full-board (vec (repeat 9 @o)))

  (it "returns [] if there is no empty spot in a given combo"
    (should= [] (rules/available-spots-in-combo @full-board [0 1 2])))

  (it "returns the available spots in a given section"
    (should= [8 11] (rules/available-spots-in-combo  [@o @_ @_ @_
                                                      @_ @_ @_ @_
                                                      @_ @x @x @_
                                                      @_ @o @_ @_]
                                                      [8 9 10 11]))))

(describe "markers-frequency"
  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :green})

  (it "returns a map with frequency of :xs, :os and empty spots in a combo"
    (should= {:x 2 :o 1 :_ 1} (rules/markers-frequency [@x @x @o @_
                                                        @_ @_ @_ @_
                                                        @_ @_ @_ @_
                                                        @_ @_ @_ @_]
                                                       [0 1 2 3]))))

(describe "marker-frequency"
  (with _ board/empty-spot)
  (with x {:token :x :color :blue})

  (it "returns 0 if there is no marker in given combo"
    (should= 0 (rules/marker-frequency (board/new-board 3)
                                       [0 1 2]
                                       @x)))

  (it "returns 2 if there is 2 @x in given combo"
    (should= 2 (rules/marker-frequency [@x @x @_
                                        @_ @_ @_
                                        @_ @_ @_]
                                        [0 1 2]
                                        @x))))

(describe "combo-to-win"
  (with _ board/empty-spot)
  (with o {:token :o :color :blue})
  (with x {:token :x :color :green})

  (it "returns nil if there is no immediate winning position"
    (should-not (rules/combo-to-win [@x @o @_
                                      @o @_ @_
                                      @_ @x @_]
                                      @x)))

  (it "returns nil if current-player cannot win"
    (should-not (rules/combo-to-win [@o @x @_
                                      @_ @o @_
                                      @_ @x @_]
                                      @x)))

  (it "returns the row where it can win"
    (should= [3 4 5] (rules/combo-to-win [@_ @o @o
                                           @x @x @_
                                           @_ @_ @_]
                                           @x)))

  (it "returns the diagonal where it can win"
    (should= [2 4 6] (rules/combo-to-win [@_ @o @x
                                           @_ @_ @o
                                           @x @_ @_]
                                           @x)))

  (it "returns the column where it can win"
    (should= [1 4 7] (rules/combo-to-win [@o @_ @_
                                           @o @x @_
                                           @_ @x @_]
                                           @x))))

(describe "place-in-winning-spot"
  (with _ board/empty-spot)
  (with o {:token :o :color :blue})
  (with x {:token :x :color :green})

  (it "returns the center spot if it will win with that move"
    (should= 4 (rules/place-in-winning-spot [@_ @o @x
                                            @_ @_ @o
                                            @x @_ @_]
                                            @x)))

  (it "returns 5 if it will win with that move"
    (should= 5 (rules/place-in-winning-spot [@_ @o @o
                                             @x @x @_
                                             @_ @_ @_]
                                             @x)))

  (it "returns 8 if it will win with that move"
    (should= 8 (rules/place-in-winning-spot [@o @x @_
                                             @_ @o @_
                                             @_ @x @_]
                                             @o)))

  (it "returns nil if player cannot win"
    (should-not (rules/place-in-winning-spot [@_ @o @x
                                              @o @_ @_
                                              @_ @x @_]
                                              @x)))

  (it "returns nil if current-player cannot win"
    (should-not (rules/place-in-winning-spot [@o @x @_
                                              @_ @o @_
                                              @_ @x @_]
                                              @x))))

(describe "owned-combos"
  (with _ board/empty-spot)
  (with o {:token :o :color :blue})
  (with x {:token :x :color :green})

  (it "returns an empty collection if there is no current-player marker on board"
    (should= [] (rules/owned-combos (board/move (board/new-board 4) 1 @o) @x @o)))

  (it "returns an empty collection if there are only mixed markers on combos"
    (should= [] (rules/owned-combos [@x @o @_ @_
                                     @o @x @_ @_
                                     @_ @x @o @_
                                     @o @o @x @_]
                                     @x
                                     @o)))

  (it "returns combos that have only current player marker, even if only one"
    (should= [[8 9 10 11] [3 6 9 12]] (rules/owned-combos [@o @_ @_ @_
                                                           @_ @_ @_ @_
                                                           @_ @x @_ @_
                                                           @_ @o @_ @_]
                                                           @x
                                                           @o)))

  (it "returns combos that have only current player marker"
    (should= [[8 9 10 11] [2 6 10 14] [3 6 9 12]]
             (rules/owned-combos [@o @_ @_ @_
                                  @_ @_ @_ @_
                                  @_ @x @x @_
                                  @_ @o @_ @_]
                                  @x
                                  @o))))

(describe "most-populated-owned-combo"
  (with _ board/empty-spot)
  (with o {:token :o :color :blue})
  (with x {:token :x :color :green})

  (it "returns the owned section with highest repetition rate"
    (should= [8 9 10 11] (rules/most-populated-owned-combo   [@o @_ @_ @_
                                                              @_ @_ @_ @_
                                                              @_ @x @x @_
                                                              @_ @o @_ @_]
                                                              @x
                                                              @o))))
