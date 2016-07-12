(ns ttt.input-validation-spec
  (:require [speclj.core :refer :all]
            [ttt.input-validation :refer :all]))

(def acceptable-roles
  (clojure.set/union acceptable-human-player
                     acceptable-easy-computer
                     acceptable-hard-computer))

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
    (should (every? is-acceptable-as-easy-computer? acceptable-easy-computer))))

(describe "is-acceptable-as-hard-computer?"
  (it "returns false if input is empty"
    (should-not (is-acceptable-as-hard-computer? "")))
  (it "returns false if input is not in acceptable-hard-computer set"
    (should-not (is-acceptable-as-hard-computer? "computer")))
  (it "returns true if input is on acceptable-hard-computer set"
    (should (every? is-acceptable-as-hard-computer? acceptable-hard-computer))))

(describe "is-valid-role-selection?"
  (it "only accepts whitelisted strings as valid input"
    (should (every? is-valid-role-selection? acceptable-roles)))
  (it "does not accept any other string"
    (should-not (is-valid-role-selection? "a"))))

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
