(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]
            [clojure.set :as set]))

(describe "board-size"
  (it "has a default size"
    (should= 3 board-size)))

(describe "board-length"
  (around [it]
    (with-redefs [board-size 4]
      board-size))
  (it "has the default value of squared board-size"
    (should= 16 board-length)))
; I think it would be a good goal to avoid the need to use `with-redefs` in your tests. The less you
; have to do this, the more flexible your code will likely be. Look into the strategy of dependency
; injection I told you about earlier.

(describe "new-board"
  (it "returns a vector"
    (should (vector? (new-board))))
; Ideally, your code would depend very on whether the board is a vector or not. Unless there was
; some extrinsic reason why the board has to be a vector, I'd avoid writing a test for it.
  (it "returns a vector of empty spots"
    (should (every? #{empty-spot} (new-board))))
  (it "returns a vector with length equal to board-length"
    (should (= board-length (count (new-board))))))

(describe "board-rows"
  (it "returns a collection that contains [0 1 2]"
    (should (some #(= [0 1 2] %) (board-rows))))
  (it "returns a collection that contains [3 4 5]"
    (should (some #(= [3 4 5] %) (board-rows))))
  (it "returns a collection that contains [6 7 8]"
    (should (some #(= [6 7 8] %) (board-rows)))))
; This is very coupled to the implementation of the board as well. If you were to change the size or
; represent it using another data structure, these tests would basically have to be rewritten. It
; might be better to just test that the fn returns <board-size> # of rows, and each row has
; <board-size> elements. It might be even better to create a board with marks, and assert that the
; marks at the spots in the returned rows match up to the rows in the created board.

(describe "board-columns"
  (it "returns a collection that contains [0 3 6]"
    (should (some #(= [0 3 6] %) (board-columns))))
  (it "returns a collection that contains [1 4 7]"
    (should (some #(= [1 4 7] %) (board-columns))))
  (it "returns a collection that contains [2 5 8]"
    (should (some #(= [2 5 8] %) (board-columns)))))

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
; So all the testing of rows, cols, diags, wining positions, it's testing things at a pretty low
; level. Outside of checking if the game has been won and which player won, basically nothing will
; use these functions. You can test those functions, and so long as those work correctly, you don't
; care about these ones. We should probably have a longer chat about this in person. But the short
; answer is the more you can avoid testing the hidden functions that are exclusively used by a
; of higher-level functions -- instead testing those higher-level functions -- the better. You'll
; write fewer tests and they'll break less often. You could also think of it as, when something
; goes wrong, in an ideal scenario, only one test fails. Multiple tests covering a single point of
; failure is redundant and leads to unnecessary extra work of maintaining them. This is a hard
; concept to practice, and I'm still getting the hang of it.

(describe "move"
  (with _ empty-spot)
  (with empty-board (new-board))
  (it "sets a value to an empty board"
    (should= [:x @_ @_ @_ @_ @_ @_ @_ @_]
             (move @empty-board 0 :x)))
  (it "sets a value in a given position after game has started"
    (should= [:x @_ @_ :o @_ :x @_ @_ @_]
             (move [:x @_ @_ :o @_ @_ @_ @_ @_] 5 :x))))
; It's impressive what you figured out using `with` and `@` to get things to work in place of using
; a `let`. I've never used those functions before. You could probably avoid the need for these
; functions if you restrict your use of `let` to individual tests, and define things for use
; across multiple tests using the ordinary `def` and `defn`. I'd have to ask someone with more
; experience whether it is necessarily any worse to do what you're doing now though. 

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

(describe "find-repetition"
  (with _ empty-spot)
  (with empty-board (new-board))
    (it "returns an empty collection if board is empty"
      (should= '()
               (find-repetition @empty-board)))
    (it "returns an empty collection if board is full and there is no winner"
      (should= '()
               (find-repetition [:x :o :x
                                 :o :x :o
                                 :o :x :o])))
    (it "identifies a single combo with indexes of repeated markers"
      (should= '([0 1 2])
               (find-repetition [:x :x :x
                                 :o @_ @_
                                 :o :o @_])))
    (it "identifies multiple combos with indexes of repeated markers"
      (should= '([3 4 5] [6 7 8])
                (find-repetition [:x :o @_
                                  :o :o :o
                                  :x :x :x]))))

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
