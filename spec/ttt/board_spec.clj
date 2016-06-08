(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]))

(describe "board-size"
  (it "has a default size"
    (should= board-size 9)
  )
)

(describe "winning-positions"
  (it "holds a vector with all winning positions"
    (should= (count winning-positions) 8)
  )
)

(describe "new-board"
  (it "makes a new game board"
    (should= (new-board) 
               { :board-struct [:_ :_ :_ :_ :_ :_ :_ :_ :_] }
    )
  )
)
