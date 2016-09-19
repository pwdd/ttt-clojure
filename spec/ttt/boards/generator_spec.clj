(ns ttt.boards.generator-spec
  (:require [speclj.core :refer :all]
            [ttt.boards.generator :as boards-generator]
            [ttt.boards.board :as board]))

(describe "possible-moves"
  (with board (board/new-board 3))
  (with _ board/empty-spot)
  (with x (:marker boards-generator/current-player))
  (with o (:marker boards-generator/opponent))

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

(describe "get-current-player"
  (with _ boards-generator/empty-spot)
  (with x (:marker boards-generator/current-player))
  (with o (:marker boards-generator/opponent))

  (it "returns current-player marker if it is current-player starts"
    (should= :x (boards-generator/get-current-player (board/new-board 3)
                                                     (:token @x)
                                                     (:token @o))))

  (it "returns current-player marker if it is its turn"
    (should= :x (boards-generator/get-current-player [@x @o @_ @_ @_ @_ @_ @_ @_]
                                                     (:token @x)
                                                     (:token @o))))

  (it "returns opponent marker if opponent starts"
    (should= :o (boards-generator/get-current-player (board/new-board 3)
                                                     (:token @o)
                                                     (:token @x))))

  (it "returns opponent marker if it is its turn"
    (should= :o (boards-generator/get-current-player [@x @_ @_ @_ @_ @_ @_ @_ @_]
                                                     (:token @x)
                                                     (:token @o)))))
