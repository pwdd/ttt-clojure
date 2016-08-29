(ns ttt.rules-spec
  (:require [speclj.core :refer :all]
            [ttt.rules :as rules]
            [ttt.board :as board]))

(describe "find-empty-row"
  (with _ board/empty-spot)
  (with x {:token :x :color :green})
  (with o {:token :o :color :blue})

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
