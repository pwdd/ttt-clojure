(ns ttt.view-spec
  (:require [speclj.core :refer :all]
            [ttt.view :refer :all]
            [clojure.string :as string]))

(describe "print-message"
  (around [it]
    (with-out-str (it)))
  (it "outputs a message to stdout"
    (should= (str "\n" (padding-spaces (count "test message")) "test message\n") (with-out-str (print-message "test message")))))

(describe "number-of-spaces"
  (around [it]
    (with-redefs [half-screen-width 60]))
  (it "returns the number of padding spaces for an empty string"
    (should= 60 (number-of-spaces 0)))
  (it "returns the number of spaces if message length is even"
    (should= 59 (number-of-spaces 2)))
  (it "returns the number of spaces if message length is odd"
    (should= 58 (number-of-spaces 3))))

(describe "padding-spaces"
  (around [it]
    (with-redefs [half-screen-width 60]))
  (it "returns an empty string if (/ message length 2.0) is equal to screen width"
    (should= "" (padding-spaces 120)))
  (it "returns a string with n repeated spaces"
    (should= (string/join (repeat 3 " ")) (padding-spaces 114)))
  (it "returns the right amount of spaces"
    (should= 44
             (count
               (padding-spaces (count "Please enter a number from 1-9: "))))))

(describe "add-padding-spaces"
  (with test-string (string/join (repeat 40 "a")))
    (it "adds padding spaces to every line of a string"
      (should= (str "\n"
                    (padding-spaces (count @test-string))
                    @test-string
                    "\n"
                    (padding-spaces (count @test-string))
                    @test-string)
               (add-padding-spaces
                 (str @test-string "\n" @test-string)))))
