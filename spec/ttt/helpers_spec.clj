(ns ttt.helpers-spec
  (:require [speclj.core :refer :all]
            [ttt.helpers :as helpers]
            [ttt.colors :as colors]
            [ttt.board :as board]))

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

(describe "get-keys-by-value"
  (with map-example {:a 1 :b 2 :c 3 "d" :d :e 3})

  (it "returns an empty collection if there is no key"
    (should= [] (helpers/get-keys-by-value @map-example "d")))

  (it "returns a collection with the key of a given value"
    (should= [:b] (helpers/get-keys-by-value @map-example 2)))

  (it "returns a collection with multiples keys that have the same given value"
    (should= [:c :e] (helpers/get-keys-by-value @map-example 3))))

(describe "remove-color"
  (it "returns string if it has not ansi color code"
    (should= "x" (helpers/remove-color "x")))

  (it "returns string without ansi color code"
    (should= "x" (helpers/remove-color (colors/colorize :blue "x"))))

  (it "returns string without any ansi color code"
    (should= "xoy"
             (helpers/remove-color (str (colors/colorize :blue "x")
                                         "o"
                                         (colors/colorize :red "y"))))))

(describe "marker-to-token"
  (with _ board/empty-spot)

  (it "returns the value associated with the key :token"
    (should= :x (helpers/marker-to-token {:token :x :color :blue})))
  
  (it "returns the empty-spot if the passed value is empty-spot"
    (should= @_ (helpers/marker-to-token @_))))
