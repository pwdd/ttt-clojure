(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all]
            [ttt.board :as board])
  (:import [ttt.player Player]))

(describe "marker"
  (with human (make-player { :role :human :marker :x }))
  (with easy-computer (make-player { :role :easy-computer :marker :e }))
  (with hard-computer (make-player { :role :hard-computer :marker :h }))
  (it "returns the marker associated with a human player"
    (should= :x (marker @human)))
  (it "returns the marker associated with an easy-computer player"
    (should= :e (marker @easy-computer)))
  (it "returns the marker associated with a hard-computer player"
    (should= :h (marker @hard-computer))))

(describe "value"
  (with human (make-player { :role :human :marker :x }))
  (with easy-computer (make-player { :role :easy-computer :marker :e }))
  (with hard-computer (make-player { :role :hard-computer :marker :h }))
  (it "returns the value associated with a human player"
    (should= -1 (value @human)))
  (it "returns the value associated with an easy-computer player"
    (should= -1 (value @easy-computer)))
  (it "returns the value associated with a hard-computer player"
    (should= 1 (value @hard-computer))))

(describe "is-ai?"
  (with human (make-player { :role :human :marker :x }))
  (with easy-computer (make-player { :role :easy-computer :marker :e }))
  (with hard-computer (make-player { :role :hard-computer :marker :h }))
  (it "returns true if player is easy-computer"
    (should (is-ai? @easy-computer)))
  (it "returns true if player is hard-computer"
    (should (is-ai? @hard-computer)))
  (it "returns false if player is not ai"
    (should-not (is-ai? @human))))

(describe "role"
  (with human (make-player { :role :human :marker :x }))
  (with easy-computer (make-player { :role :easy-computer :marker :e }))
  (with hard-computer (make-player { :role :hard-computer :marker :h }))
  (it "returns :human if player role is human"
    (should= :human (role @human)))
  (it "returns :easy-computer if player role is easy computer"
    (should= :easy-computer (role @easy-computer)))
  (it "returns :hard-computer if player role is hard computer"
    (should= :hard-computer (role @hard-computer))))

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

(describe "define-player"
  (with human (make-player { :marker "x" :role :human }))
  (with easy-computer (make-player { :role :easy-computer :marker "o" }))
  (with hard-computer (make-player { :role :hard-computer :marker "h" }))
  (it "returns player with type 'human' and its marker"
    (should= @human
             (define-player { :marker "x" :role "h" })))
  (it "returns player with type 'easy-computer' and its marker"
    (should= @easy-computer
             (define-player { :marker "o" :role "ec" })))
  (it "returns player with type 'hard-computer' and its marker"
    (should= @hard-computer
             (define-player { :marker "h" :role "hc" }))))
