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

(describe "valid-selection"
  (it "only accepts 'h' 'c' 'human' 'computer' as valid"
    (should (every? valid-selection? acceptable-roles)))
  (it "does not accept any other string"
    (should-not (valid-selection? "a"))))

(describe "define-player"
  (it "returns player with type 'human' and its marker"
    (should= human
             (with-in-str "x\nh" (define-player "set marker" ""))))
  (it "returns player with type 'computer' and its marker"
    (should= computer
             (with-in-str "o\nec" (define-player "set marker" "")))))

(describe "player-spot"
  (it "gets user spot"
    (should= 3 (with-in-str "4" (player-spot human))))
  (it "gets computer spot"
    (should (integer? (player-spot computer)))))

(describe "game-type"
  (it "returns :human-computer if game is human vs computer"
    (should= :human-computer (game-type human computer)))
  (it "returns :human-computer if game is computer vs human"
    (should= :human-computer (game-type computer human)))
  (it "returns nil if game is computer vs computer"
    (should-not (game-type computer computer)))
  (it "returns nil if game is human vs human"
    (should-not (game-type human human))))
