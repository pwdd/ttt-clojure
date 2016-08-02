(ns ttt.input-validation-spec
  (:require [speclj.core :refer :all]
            [ttt.input-validation :refer :all]
            [ttt.board :as board]))

(describe "is-acceptable-as-human-player?"
  (it "returns false if input is empty"
    (should-not (is-acceptable-as-human-player? "")))

  (it "returns false if input is not in acceptable-human-player set"
    (should-not (is-acceptable-as-human-player? "easy computer")))

  (it "returns true if input is in acceptable-human-player set"
    (should (every? is-acceptable-as-human-player? acceptable-human-player))))

(describe "is-acceptable-as-easy-computer?"
  (it "returns false if input is empty"
    (should-not (is-acceptable-as-easy-computer? "")))

  (it "returns false if input is not in acceptable-easy-computer set"
    (should-not (is-acceptable-as-easy-computer? "hard computer")))

  (it "returns true if input is on acceptable-easy-computer set"
    (should (every? is-acceptable-as-easy-computer?
                    acceptable-easy-computer))))

(describe "is-acceptable-as-hard-computer?"
  (it "returns false if input is empty"
    (should-not (is-acceptable-as-hard-computer? "")))

  (it "returns false if input is not in acceptable-hard-computer set"
    (should-not (is-acceptable-as-hard-computer? "computer")))

  (it "returns true if input is on acceptable-hard-computer set"
    (should (every? is-acceptable-as-hard-computer? acceptable-hard-computer))))

(describe "is-valid-role-selection?"

  (with acceptable-roles (clojure.set/union acceptable-human-player
                                            acceptable-easy-computer
                                            acceptable-hard-computer)

  (it "only accepts whitelisted strings as valid input"
    (should (every? is-valid-role-selection? @acceptable-roles)))

  (it "does not accept any other string"
    (should-not (is-valid-role-selection? "a")))))

(describe "is-valid-marker?"
  (it "returns false if input is empty"
    (should-not (is-valid-marker? " " "")))

  (it "returns true if input is a single character"
    (should (is-valid-marker? "a" "")))

  (it "returns true if input is a capitalized character"
    (should (is-valid-marker? "B" "")))

  (it "returns false if input is a numeric string"
    (should-not (is-valid-marker? "0" "")))

  (it "returns false if input has more than one character"
    (should-not (is-valid-marker? "ab" "")))

  (it "returns false if chosen marker is the same as opponent's marker"
    (should-not (is-valid-marker? "a" "a"))))

(describe "is-int?"
  (it "returns true if argument is numeric string"
    (should (is-int? "5")))

  (it "returns true if argument is numeric string with whitespaces"
    (should (is-int? "  1 ")))

  (it "returns false if argument is not a numeric string"
    (should-not (is-int? "a")))

  (it "returns false if argument is a word"
    (should-not (is-int? "parakeet"))))

(describe "is-valid-move-input?"

  (with _ board/empty-spot)

  (it "returns false if input is not an integer string"
    (should-not (is-valid-move-input? [@_ @_ :o :x :x :o @_ @_ @_] "a")))

  (it "returns true if input is an integer and is a valid move"
    (should (is-valid-move-input? [@_ @_ :o :x :x :o @_ @_ @_] "1")))

  (it "returns false if spot has a marker"
    (should-not (is-valid-move-input? [@_ @_ :o :x :x :o @_ @_ @_] "3"))))

(describe "is-valid-new-or-saved?"
  (it "returns false if input is a letter"
    (should-not (is-valid-new-or-saved? "a")))

  (it "returns false if input is '0'"
    (should-not (is-valid-new-or-saved? "0")))

  (it "returns false if input has more than one letter"
    (should-not (is-valid-new-or-saved? "12")))

  (it "returns true if input is '1'"
    (should (is-valid-new-or-saved? "1")))

  (it "returns true if input is '2'"
    (should (is-valid-new-or-saved? "2"))))

(describe "is-valid-filename?"

  (with filenames ["hh" "hchc" "a-filename"])

  (it "returns false if input is not in the argument array"
    (should-not (is-valid-filename? "a" @filenames)))

  (it "returns true if input is in argument array"
    (should (is-valid-filename? "hchc" @filenames))))
