(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :refer :all]))

(describe "translate-keyword"
  (it "returns ' x '"
    (should= " x " (translate-keyword :x)))
  (it "returns ' o '"
    (should= " o " (translate-keyword :o)))
  (it "returns an empty space for :_"
    (should= "   " (translate-keyword :_))))

(describe "is-int?"
  (it "returns true if string is 0"
    (should (is-int? "0")))
  (it "returns true if string is 5"
    (should (is-int? "5")))
  (it "returns true if string is '  1 '"
    (should (is-int? "  1 ")))
  (it "returns false if string is 'a'"
    (should-not (is-int? "a")))
  (it "returns false if string is '   a '"
    (should-not (is-int? "a")))
  (it "returns false if string is 'parakeet'"
    (should-not (is-int? "parakeet"))))

(describe "input-to-number"
  (it "returns 0 when input is '1'"
    (should= 0 (input-to-number "1")))
  (it "returns 8 when input is '9'"
    (should= 8 (input-to-number "9"))))
