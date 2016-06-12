(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :refer :all]))

; test pass even with "x"
(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x " (translate-keyword :x)))
  (it "returns ' o '"
    (should= " o " (translate-keyword :o)))
  (it "returns an empty space for :_"
    (should= "   " (translate-keyword :_))))

; (describe "input-to-number"
;   (it "returns 0 when input is '1'"
;     (should= 0 (input-to-number "1"))
;   )
; )
