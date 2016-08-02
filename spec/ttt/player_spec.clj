(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all]
            [ttt.board :as board])
  (:import [ttt.player Player]))

(describe "is-ai?"
  (it "returns true if player is easy-computer"
    (should (is-ai? :easy-computer)))

  (it "returns true if player is hard-computer"
    (should (is-ai? :hard-computer)))

  (it "returns false if player is not ai"
    (should-not (is-ai? :human))))

(describe "define-player"
  (it "returns an instance of Player"
    (should (instance? Player (define-player { :marker "x" :role "h" }))))

  (it "returns player with role ':human' and its marker"
    (should= :human
             (:role (define-player { :marker "x" :role "h" }))))

  (it "returns player with role ':easy-computer' and its marker"
    (should= :easy-computer
             (:role (define-player { :marker "o" :role "ec" }))))

  (it "returns player with role ':hard-computer' and its marker"
    (should= :hard-computer
             (:role (define-player { :marker "h" :role "hc" })))))
