(ns ttt.core-spec
  (:require [speclj.core :refer :all]
            [ttt.core :refer [board-representation]]))

(describe "board-representation"

  (it "shows representation of board positions"
  
    (should= (with-out-str (board-representation)) " 1 | 2 | 3 \r\n---|---|---\r\n 4 | 5 | 6 \r\n---|---|---\r\n 7 | 8 | 9 \n\r\n"))
  )

