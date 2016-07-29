(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :refer :all]))

(describe "clean-string"
  (it "trims whitespaces"
    (should= "abc" (clean-string " abc ")))
  (it "lower-case entire string"
    (should= "abc" (clean-string "aBc"))))

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

(describe "stringify-role"
  (it "returns 'easy' if player is easy computer"
    (should= "easy" (stringify-role :easy-computer)))
  (it "returns 'hard' if player is hard computer"
    (should= "hard" (stringify-role :hard-computer)))
  (it "returns 'human' if player is human"
    (should= "human" (stringify-role :human))))

(describe "write-game-type"
  (it "returns a keyword"
    (should (keyword? (write-game-type "easy" "human"))))
  (it "returns an alphabetically ordered keyword, does not matter the order of arguments"
    (should= :easy-x-hard (write-game-type "hard" "easy"))))

(describe "clean-filenames"
  (with filenames ["abc.json" "def.json" "something-something.json"])
  (it "returns a collection of strings"
    (should (and (coll? (clean-filenames @filenames))
                 (every? string? @filenames))))
  (it "returns a collection that contains 'abc'"
    (should (some #{"abc"} (clean-filenames @filenames))))
  (it "returns a collection that contains 'def'"
    (should (some #{"def"} (clean-filenames @filenames))))
  (it "returns a collections of strings that do not have file extension"
    (should= [] (filter #(re-find #"\." %) (clean-filenames @filenames)))))
