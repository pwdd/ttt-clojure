(ns ttt.view-spec
  (:require [speclj.core :refer :all]
            [ttt.view :as view]
            [clojure.string :as string]
            [ttt.colors :as colors]))

(describe "number-of-spaces"

  (it "returns the number of padding spaces for an empty string"
    (should= 60 (view/number-of-spaces 0 60)))

  (it "returns the number of spaces if message length is even"
    (should= 59 (view/number-of-spaces 2 60)))

  (it "returns the number of spaces if message length is odd"
    (should= 59 (view/number-of-spaces 3 60))))

(describe "color-code-list"
  (it "returns false if string has no color code"
     (should-not (view/color-code-list "foo")))

  (it "returns a collection with codes if string has one part that has color"
    (should= ["[34m" "[0;37m"]
             (view/color-code-list (str (:blue colors/ansi-colors)
                                        "foo bar"
                                        (:default colors/ansi-colors))))))

(describe "color-code-length"
  (it "returns 0 if string has not color code"
    (should (zero? (view/color-code-length "foo")))

    (it "returns the length of a set ansi color code used on string"
      (should= 8 (view/color-code-length (str (:blue colors/ansi-colors)
                                               "foo bar"
                                               (:default colors/ansi-colors)))))

    (it "returns the length of two sets of ansi color code used on string"
      (should= 16 (view/color-code-length (str (:blue colors/ansi-colors)
                                                "foo"
                                                (:default colors/ansi-colors)
                                                "bar"
                                                (:green colors/ansi-colors)
                                                "baz"
                                                (:default colors/ansi-colors)))))))

(describe "padding-spaces"

  (it "returns an empty string if (/ message length 2.0) is equal to screen width"
    (should= "" (view/padding-spaces 120 60)))

  (it "returns a string with n repeated spaces"
    (should= (string/join (repeat 3 " ")) (view/padding-spaces 114 60)))

  (it "returns the right amount of spaces"
    (should= 44
             (count (view/padding-spaces (count "Please enter a number from 1-9: ") 60)))))

(describe "add-padding-spaces"

  (with test-string (string/join (repeat 40 "a")))

  (it "adds padding spaces to every line of a string"
    (should= (str "\n"
                  (view/padding-spaces (count @test-string) 60)
                  @test-string
                  "\n"
                  (view/padding-spaces (count @test-string) 60)
                  @test-string)
             (view/add-padding-spaces (str @test-string "\n" @test-string) 60))))
