(ns ttt.mocks-spec
  (:require [speclj.core :refer :all]
            [ttt.mocks :refer :all]
            [ttt.player :refer :all])
  (:import [ttt.player Player]))

(describe "mock-make-player"
  (it "returns an instance of Player"
    (should (instance? Player (mock-make-player { :role :easy-computer
                                             :marker "A" }))))
  (it "returns Player that has :ai attribute"
    (should= true (:ai (mock-make-player { :role :easy-computer :marker "a" }))))
  (it "returns Player that has :value attribute"
    (should= -1 (:value (mock-make-player { :role :easy-computer :marker "c" }))))
  (it "returns a Player that has a :role attribute"
    (should= :hard-computer
             (:role (mock-make-player { :role :hard-computer :marker "a" })))))
