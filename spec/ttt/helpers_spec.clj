(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :refer :all]))

(describe "spot-to-string"
  (it "returns ' 1 ' if integer is 0"
    (should= " 1 " (spot-to-string 0))
  )
  (it "converts integer into its string representation on board"
    (should= " 2 " (spot-to-string 1))
  )
)

(describe "keyword-to-string"
  (it "returns ' x '"
    (should= " x " (keyword-to-string :x))
  )
  (it "returns ' o '"
    (should= " o " (keyword-to-string :o))
  )
)

(describe "to-string"
  (it "uses keyword-to-string if element is string"
    (should= " x " (to-string :x))
  )
  (it "uses spot-to-string if element is integer"
    (should= " 1 " (to-string 0))
  )
)


