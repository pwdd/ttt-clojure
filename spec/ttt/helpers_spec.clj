(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :refer :all]))

(describe "clean-string"
  (it "trims whitespaces"
    (should= "abc" (clean-string " abc ")))
  (it "lower-case entire string"
    (should= "abc" (clean-string "aBc"))))

(describe "is-int?"
  (it "returns true if argument is numeric string"
    (should (is-int? "5")))
  (it "returns true if argument is numeric string with whitespaces"
    (should (is-int? "  1 ")))
  (it "returns false if argument is not a numeric string"
    (should-not (is-int? "a")))
  (it "returns false if argument is a word"
    (should-not (is-int? "parakeet"))))

(describe "input-to-number"
  (it "returns integer 0 when input is string '1'"
    (should= 0 (input-to-number "1")))
  (it "returns integer 8 when input is string '9'"
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
  (it "returns true if 10 and if limit is 20"
     (should (in-range? 10 20))))

(describe "write-game-type"
  (it "returns a keyword"
    (should (keyword? (write-game-type "easy" "human"))))
  (it "returns an alphabetically ordered keyword, does not matter the order of arguments"
    (should= :easy-x-hard (write-game-type "hard" "easy"))))
