(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :as helpers]
            [ttt.colors :as colors]))

(describe "clean-string"

  (it "trims whitespaces"
    (should= "abc" (helpers/clean-string " abc ")))

  (it "lower-case entire string"
    (should= "abc" (helpers/clean-string "aBc"))))

(describe "input-to-number"
  (it "returns integer 0 when input is string '1'"
    (should= 0 (helpers/input-to-number "1")))

  (it "returns integer 8 when input is string '9'"
    (should= 8 (helpers/input-to-number "9"))))

(describe "in-range?"
  (it "returns true if index is in range"
    (should (helpers/in-range? 2 8)))

  (it "returns false if index is not in range"
    (should-not (helpers/in-range? 10 8)))

  (it "returns true if 0"
    (should (helpers/in-range? 0 8)))

  (it "returns false if 9"
    (should-not (helpers/in-range? 9 8)))

  (it "returns true if 10 and if limit is 20"
     (should (helpers/in-range? 10 20))))

(describe "remove-color"
  (it "returns string if it has not ansi color code"
    (should= "x" (helpers/remove-color "x")))

  (it "returns string without ansi color code"
    (should= "x" (helpers/remove-color (colors/colorize :blue "x"))))

  (it "returns string without any ansi color code"
    (should= "xoy"
             (helpers/remove-color (str (colors/colorize :blue "x")
                                         "o"
                                         (colors/colorize :red "y")))))
)
