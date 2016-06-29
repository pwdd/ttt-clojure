(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all])
  (:import [ttt.player Player]))

(def human (->Player "a" :human false -1))
(def computer (->Player "x" :easy-computer true 1))

(describe "player-marker"
  (it "returns the marker associated with a human player"
    (should= "a" (player-marker human)))
  (it "returns the marker associated with a computer player"
    (should= "x" (player-marker computer))))

(describe "player-value"
  (it "returns the value associated with a human player"
    (should= -1 (player-value human)))
  (it "returns the value associate with a computer player"
    (should= 1 (player-value computer))))

(describe "is-ai?"
  (it "returns true if player is ai"
    (should (is-ai? computer)))
  (it "returns false if player is not ai"
    (should-not (is-ai? human))))

(describe "player-role"
  (it "returns :human if player role is human"
    (should= :human (player-role human)))
  (it "returns :easy-computer if player role is easy computer"
    (should= :easy-computer (player-role computer))))

(describe "make-player"
  (it "returns an instance of Player"
    (should (instance? Player (make-player { :role :easy-computer
                                             :marker "A" }))))
  (it "returns Player that has :ai attribute"
    (should= true (:ai (make-player {:role :easy-computer :marker "a"}))))
  (it "returns Player that has :value attribute"
    (should= 1 (:value (make-player {:role :easy-computer :marker "c"})))))
