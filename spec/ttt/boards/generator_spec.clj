(ns ttt.boards.generator-spec
  (:require [speclj.core :refer :all]
            [ttt.boards.generator :as boards-generator]
            [ttt.boards.board :as board]))

(describe "possible-moves"
  (with board (board/new-board 3))
  (with _ board/empty-spot)
  (with x boards-generator/current-player)
  (with o boards-generator/opponent)

  (it "returns a collection with length equal to lenght of passed board"
    (should= (count @board)
             (count (boards-generator/possible-moves @board @x))))

  (it "has a board with marker on index 0"
    (should-contain [@x @_ @_ @_ @_ @_ @_ @_ @_]
                    (boards-generator/possible-moves @board @x)))

  (it "has a board with a marker on index 1"
    (should-contain [@_ @x @_ @_ @_ @_ @_ @_ @_]
                    (boards-generator/possible-moves @board @x)))

  (it "does not have repeated boards"
    (let [boards (boards-generator/possible-moves @board @x)]
      (should= (count boards) (count (distinct boards))))))
