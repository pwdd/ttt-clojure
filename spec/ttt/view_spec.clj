(ns ttt.view-spec
  (:require [speclj.core :refer :all]
            [ttt.view :refer :all]))

(describe "print-message"
  (around [it]
    (with-out-str (it)))
  (it "outputs a message to stdout"
    (should= (str "\n" (padding-spaces "test message") "test message\r\n") (with-out-str (print-message "test message")))))

(describe "number-of-spaces"
  (it "returns the number of padding spaces for an empty string"
    (should= 60 (number-of-spaces 0 60)))
  (it "returns the number of spaces if message length is even"
    (should= 59 (number-of-spaces 2 60)))
  (it "returns the number of spaces if message length is odd"
    (should= 58 (number-of-spaces 3 60))))

(describe "padding-spaces"
  (it "returns an empty string if (/ message length 2.0) is equal to screen width"
    (should= "" (padding-spaces (clojure.string/join (repeat 120 "a")))))
  (it "returns a string with n repeated spaces"
    (should= "   " (padding-spaces (clojure.string/join (repeat 114 "a")))))
  (it "returns the right amount of spaces"
    (should= 44
             (count (padding-spaces "Please enter a number from 1-9: ")))))

(describe "add-padding-spaces"
  (let [test-string (clojure.string/join (repeat 40 "a"))]
    (it "adds padding spaces to every line of a string"
      (should= (str "\n"
                    (padding-spaces test-string)
                    test-string
                    "\n"
                    (padding-spaces test-string)
                    test-string)
               (add-padding-spaces
                 (str test-string "\n" test-string))))))
