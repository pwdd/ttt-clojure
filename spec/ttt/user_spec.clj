(ns ttt.user-spec
  (:require [speclj.core :refer :all]
            [ttt.user :refer :all]))

(describe "get-user-spot"
  (it "returns 0 if input is '1'"
    (should= 0 (with-in-str "1" (get-user-spot))))
  (it "returns 8 if input is 9"
    (should= 8 (with-in-str "9" (get-user-spot)))))
