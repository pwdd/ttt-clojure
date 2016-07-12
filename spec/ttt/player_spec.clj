(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all])
  (:import [ttt.player Player]))

(describe "marker"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns the marker associated with a human player"
      (should= :x (marker human)))
    (it "returns the marker associated with an easy-computer player"
      (should= :e (marker easy-computer)))
    (it "returns the marker associated with a hard-computer player"
      (should= :h (marker hard-computer)))))

(describe "value"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns the value associated with a human player"
      (should= -1 (value human)))
    (it "returns the value associated with an easy-computer player"
      (should= -1 (value easy-computer)))
    (it "returns the value associated with a hard-computer player"
      (should= 1 (value hard-computer)))))

(describe "is-ai?"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns true if player is easy-computer"
      (should (is-ai? easy-computer)))
    (it "returns true if player is hard-computer"
      (should (is-ai? hard-computer)))
    (it "returns false if player is not ai"
      (should-not (is-ai? human)))))

(describe "role"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns :human if player role is human"
      (should= :human (role human)))
    (it "returns :easy-computer if player role is easy computer"
      (should= :easy-computer (role easy-computer)))
    (it "returns :hard-computer if player role is hard computer"
      (should= :hard-computer (role hard-computer)))))

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
