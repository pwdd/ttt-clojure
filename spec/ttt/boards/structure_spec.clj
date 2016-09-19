(ns ttt.boards.structure-spec
  (:require [speclj.core :refer :all]
            [ttt.boards.structure :as boards-structure]
            [ttt.boards.generator :as boards-generator]
            [ttt.get-spots :refer [select-spot]]))


(describe "board-unit"
  (with _ boards-generator/empty-spot)
  (with x {:token :x})
  (with o {:token :o})
  (with board (vec (repeat 9 @_)))
  (with another-board [@o @o @_
                       @x @x @_
                       @_ @_ @_])

  (it "returns map with a 'board' key"
    (should (:board (boards-structure/board-unit @board))))

  (it "returns a map with a 'best-move' key"
    (should (:best-move (boards-structure/board-unit @board) :best-move)))

  (it "has a value for 'best-move' that is the best move for an empty board"
    (should= 4 (:best-move (boards-structure/board-unit @board))))

  (it "has the best move for any board"
    (should= 5 (:best-move (boards-structure/board-unit @another-board)))))

