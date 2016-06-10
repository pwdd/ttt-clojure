(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :refer :all]))

(describe "board-representation"
  (it "outputs a numbered representation of the board if no spot is taken"
    (should=
      " 1 | 2 | 3 \n---|---|---\n 4 | 5 | 6 \n---|---|---\n 7 | 8 | 9 \n"
      (board-representation [0 1 2 3 4 5 6 7 8])
    )
  )
  (it "combines numbers and letters when some spots are taken"
    (should=
      " 1 | x | 3 \n---|---|---\n o | 5 | 6 \n---|---|---\n 7 | 8 | x \n"
      (board-representation [0 :x 2 :o 4 5 6 7 :x])
    )
  )
)

(describe "separator"
  (it "has a default value"
    (should= "\n---|---|---\n" separator)
  )
)
