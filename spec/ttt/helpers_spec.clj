(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :refer :all]))

(describe "is-int?"
  (it "returns true if argument is numreric string"
    (should (is-int? "5")))
  (it "returns true if argument is numeric string with whitespaces"
    (should (is-int? "  1 ")))
  (it "returns false if argument is not a numeric string"
    (should-not (is-int? "a")))
  (it "returns false if argument is a word"
    (should-not (is-int? "parakeet"))))

(describe "input-to-number"
  (it "returns 0 when input is '1'"
    (should= 0 (input-to-number "1")))
  (it "returns 8 when input is '9'"
    (should= 8 (input-to-number "9"))))

(describe "in-range?"
  (it "returns true if index is in range"
    (should (in-range? 2 8)))
  (it "returns false if index is not in range"
    (should-not (in-range? 10 8)))
  (it "returns true if 0"
    (should (in-range? 0 8)))
  (it "returns false if 9"
    (should-not (in-range? 9 8)))
  (it "returns true for 10 if limit is 20"
     (should (in-range? 10 20))))

(describe "clean-string"
  (it "trims whitespaces"
    (should= "abc" (clean-string " abc ")))
  (it "lower-case entire string"
    (should= "abc" (clean-string "aBc"))))

(describe "valid-marker?"
  (it "returns false if input is empty"
    (should-not (valid-marker? " " "")))
  (it "returns true if input is a single character"
    (should (valid-marker? "a" "")))
  (it "returns true if input is a capitalized character"
    (should (valid-marker? "B" "")))
  (it "returns false if input is a numeric string"
    (should-not (valid-marker? "0" "")))
  (it "returns false if input has more than one character"
    (should-not (valid-marker? "ab" "")))
  (it "returns false if chosen marker is the same as opponent's marker"
    (should-not (valid-marker? "a" "a"))))
