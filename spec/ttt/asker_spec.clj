(ns ttt.asker-spec
  (:require [speclj.core :refer :all]
            [ttt.asker :refer :all]))

(describe "valid-selection?"
  (let [acceptable ["h" "c" "human" "computer"]]
    (it "only accepts 'h' 'c' 'human' 'computer' as valid"
      (should (every? valid-selection? acceptable))))
  (it "does not accept any other string"
    (should-not (valid-selection? "a"))))

(describe "who-plays"
  (it "returns 'h' if user chooses 'human'"
    (should= "h" (with-in-str "h" (who-plays))))
  (it "returns 'h' if user spells out 'human'"
    (should= "h" (with-in-str "hUmAn" (who-plays))))
  (it "returns 'c' if user chooses computer"
    (should= "c" (with-in-str "c" (who-plays))))
  (it "returns 'c' if user spells 'computer'"
    (should= "c" (with-in-str "coMpUteR" (who-plays)))))

(describe "define-player"
  (it "returns player with type 'human' and its marker"
    (should= {:type :human :marker :x}
             (with-in-str "h" (define-player :x))))
  (it "returns player with type 'computer' and its marker"
    (should= {:type :computer :marker :x}
             (with-in-str "c" (define-player :x)))))

(describe "get-user-spot"
  (it "returns 0 if input is '1'"
    (should= 0 (with-in-str "1" (get-user-spot))))
  (it "returns 8 if input is 9"
    (should= 8 (with-in-str "9" (get-user-spot)))))
