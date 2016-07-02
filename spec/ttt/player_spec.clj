(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all])
  (:import [ttt.player Player]))

(def human (make-player { :role :human :marker :x }))
(def easy-computer (make-player { :role :easy-computer :marker :e }))
(def hard-computer (make-player { :role :hard-computer :marker :h }))

(describe "marker"
  (it "returns the marker associated with a human player"
    (should= :x (marker human)))
  (it "returns the marker associated with an easy-computer player"
    (should= :e (marker easy-computer)))
  (it "returns the marker associated with a hard-computer player"
    (should= :h (marker hard-computer))))

(describe "value"
  (it "returns the value associated with a human player"
    (should= -1 (value human)))
  (it "returns the value associated with an easy-computer player"
    (should= -1 (value easy-computer)))
  (it "returns the value associated with a hard-computer player"
    (should= 1 (value hard-computer))))

(describe "is-ai?"
  (it "returns true if player is easy-computer"
    (should (is-ai? easy-computer)))
  (it "returns true if player is hard-computer"
    (should (is-ai? hard-computer)))
  (it "returns false if player is not ai"
    (should-not (is-ai? human))))

(describe "role"
  (it "returns :human if player role is human"
    (should= :human (role human)))
  (it "returns :easy-computer if player role is easy computer"
    (should= :easy-computer (role easy-computer)))
  (it "returns :hard-computer if player role is hard computer"
    (should= :hard-computer (role hard-computer))))

(describe "make-player"
  (it "returns an instance of Player"
    (should (instance? Player (make-player { :role :easy-computer
                                             :marker "A" }))))
  (it "returns Player that has :ai attribute"
    (should= true (:ai (make-player { :role :easy-computer :marker "a" }))))
  (it "returns Player that has :value attribute"
    (should= -1 (:value (make-player { :role :easy-computer :marker "c" }))))
  (it "returns a Player that has a :role attribute"
    (should= :hard-computer
             (:role (make-player { :role :hard-computer :marker "a" })))))

(describe "select-spot :easy-computer"
  (it "returns an integer from 0 to board-length exclusive"
   (should (and (>= (select-spot easy-computer { :board-length 9 }) 0)
                (< (select-spot easy-computer { :board-length 9 }) 9)))))

(describe "select-spot :human"
  (it "returns 0 if input is '1'"
    (should= 0 (with-in-str "1" (select-spot human))))
  (it "returns 8 if input is 9"
    (should= 8 (with-in-str "9" (select-spot human)))))
