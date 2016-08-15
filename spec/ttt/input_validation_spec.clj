(ns ttt.input-validation-spec
  (:require [speclj.core :refer :all]
            [ttt.input-validation :as input-validation]
            [ttt.board :as board]
            [clojure.set :as set]))

(describe "is-acceptable-as-human-player?"
  (it "returns false if input is empty"
    (should-not (input-validation/is-acceptable-as-human-player? "")))

  (it "returns false if input is not in acceptable-human-player set"
    (should-not (input-validation/is-acceptable-as-human-player? "easy computer")))

  (it "returns true if input is in acceptable-human-player set"
    (should (every? input-validation/is-acceptable-as-human-player?
                    input-validation/acceptable-human-player))))

(describe "is-acceptable-as-easy-computer?"
  (it "returns false if input is empty"
    (should-not (input-validation/is-acceptable-as-easy-computer? "")))

  (it "returns false if input is not in acceptable-easy-computer set"
    (should-not (input-validation/is-acceptable-as-easy-computer? "hard computer")))

  (it "returns true if input is on acceptable-easy-computer set"
    (should (every? input-validation/is-acceptable-as-easy-computer?
                    input-validation/acceptable-easy-computer))))

(describe "is-acceptable-as-hard-computer?"
  (it "returns false if input is empty"
    (should-not (input-validation/is-acceptable-as-hard-computer? "")))

  (it "returns false if input is not in acceptable-hard-computer set"
    (should-not (input-validation/is-acceptable-as-hard-computer? "computer")))

  (it "returns true if input is on acceptable-hard-computer set"
    (should (every? input-validation/is-acceptable-as-hard-computer?
                    input-validation/acceptable-hard-computer))))

(describe "is-valid-role-selection?"

  (with acceptable-roles (set/union input-validation/acceptable-human-player
                                    input-validation/acceptable-easy-computer
                                    input-validation/acceptable-hard-computer)

  (it "only accepts whitelisted strings as valid input"
    (should (every? input-validation/is-valid-role-selection? @acceptable-roles)))

  (it "does not accept any other string"
    (should-not (input-validation/is-valid-role-selection? "a")))))

(describe "is-valid-marker?"
  (it "returns false if input is empty"
    (should-not (input-validation/is-valid-marker? " " "")))

  (it "returns true if input is a single character"
    (should (input-validation/is-valid-marker? "a" "")))

  (it "returns true if input is a capitalized character"
    (should (input-validation/is-valid-marker? "B" "")))

  (it "returns false if input is a numeric string"
    (should-not (input-validation/is-valid-marker? "0" "")))

  (it "returns false if input has more than one character"
    (should-not (input-validation/is-valid-marker? "ab" "")))

  (it "returns false if chosen token is the same as opponent's token"
    (should-not (input-validation/is-valid-marker? "a" :a))))

(describe "is-int?"
  (it "returns true if argument is numeric string"
    (should (input-validation/is-int? "5")))

  (it "returns true if argument is numeric string with whitespaces"
    (should (input-validation/is-int? "  1 ")))

  (it "returns false if argument is not a numeric string"
    (should-not (input-validation/is-int? "a")))

  (it "returns false if argument is a word"
    (should-not (input-validation/is-int? "parakeet"))))

(describe "is-valid-move-input?"

  (with _ board/empty-spot)

  (it "returns false if input is not an integer string"
    (should-not (input-validation/is-valid-move-input?
                   [@_ @_ :o :x :x :o @_ @_ @_] "a")))

  (it "returns true if input is an integer and is a valid move"
    (should (input-validation/is-valid-move-input?
              [@_ @_ :o :x :x :o @_ @_ @_] "1")))

  (it "returns false if spot has a marker"
    (should-not (input-validation/is-valid-move-input?
                  [@_ @_ :o :x :x :o @_ @_ @_] "3"))))

(describe "is-valid-new-or-saved?"
  (it "returns false if input is a letter"
    (should-not (input-validation/is-valid-new-or-saved? "a")))

  (it "returns false if input is '0'"
    (should-not (input-validation/is-valid-new-or-saved? "0")))

  (it "returns false if input has more than one letter"
    (should-not (input-validation/is-valid-new-or-saved? "12")))

  (it "returns true if input is '1'"
    (should (input-validation/is-valid-new-or-saved? "1")))

  (it "returns true if input is '2'"
    (should (input-validation/is-valid-new-or-saved? "2"))))

(describe "is-valid-filename?"

  (with filenames ["hh" "hchc" "a-filename"])

  (it "returns false if input is not in the argument array"
    (should-not (input-validation/is-valid-filename? "a" @filenames)))

  (it "returns true if input is in argument array"
    (should (input-validation/is-valid-filename? "hchc" @filenames))))

(describe "save?"
  (it "returns true if input is equal to 'save-valid-input'"
    (should (input-validation/save? "save")))

  (it "returns alse if input is not equal to 'save-valid-input'"
    (should-not (input-validation/save? "s"))))
