(ns ttt.game-spec
  (:require [speclj.core :refer :all]
           [ttt.game :refer :all]
           [ttt.player :refer :all])
  (:import [ttt.player Player]))

(def human (->Player :x false -1))
(def computer (->Player :o true 1))

(describe "valid-selection"
  (let [acceptable ["h" "c" "human" "computer"]]
    (it "only accepts 'h' 'c' 'human' 'computer' as valid"
      (should (every? valid-selection? acceptable))))
  (it "does not accept any other string"
    (should-not (valid-selection? "a"))))

(describe "define-player"
  (it "returns player with type 'human' and its marker"
    (should= human
             (with-in-str "h" (define-player :x))))
  (it "returns player with type 'computer' and its marker"
    (should= computer
             (with-in-str "c" (define-player :o)))))

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
