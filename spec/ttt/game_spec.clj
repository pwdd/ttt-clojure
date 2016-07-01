(ns ttt.game-spec
  (:require [speclj.core :refer :all]
           [ttt.game :refer :all]
           [ttt.player :refer [make-player]]))

(def human (make-player {:marker "x" :role :human}))
(def computer (make-player {:marker "o" :role :easy-computer}))

(def acceptable-roles
  (clojure.set/union acceptable-human-player
                     acceptable-easy-computer
                     acceptable-hard-computer))

(describe "valid-selection?"
  (it "only accepts whitelisted strings as valid input"
    (should (every? valid-selection? acceptable-roles)))
  (it "does not accept any other string"
    (should-not (valid-selection? "a"))))

(describe "valid-marker?"
  (it "returns false if input is empty"
    (should-not (valid-marker? " " "")))
  (it "returns true if input is a single character"
    (should (valid-marker? "a" "")))
  (it "returns true if input is a capitalized character"
    (should (valid-marker? "B" "")))
  (it "returns false if input is a numeric string"
    (should-not (valid-marker? "0" "")))
  (it "returns false if input has more than one character"
    (should-not (valid-marker? "ab" "")))
  (it "returns false if chosen marker is the same as opponent's marker"
    (should-not (valid-marker? "a" "a"))))

(describe "define-player"
  (it "returns player with type 'human' and its marker"
    (should= human
             (with-in-str "x\nh" (define-player { :msg "set marker" }))))
  (it "returns player with type 'computer' and its marker"
    (should= computer
             (with-in-str "o\nec" (define-player { :msg "set marker" })))))

(describe "player-spot"
  (it "gets user spot"
    (should= 3 (with-in-str "4" (player-spot [:_ :_ :_ :_ :_ :_ :_ :_ :_]
                                             human
                                             computer))))
  (it "gets computer spot"
    (should (integer? (player-spot [:_ :_ :_ :_ :_ :_ :_ :_ :_]
                                   computer
                                   human)))))
