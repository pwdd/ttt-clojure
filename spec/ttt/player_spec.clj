(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all])
  (:import [ttt.player Player]))

(def human (->Player "a" false -1))
(def computer (->Player "x" true 1))

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

(describe "make-player"
  (it "returns an instance of Player"
    (should (instance? Player (make-player { :ai true
                                             :marker "A" })))))
