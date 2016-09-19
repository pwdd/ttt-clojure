(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :as player]
            [ttt.boards.board :as board])
  (:import [ttt.player Player]))

(describe "is-ai?"
  (it "returns true if player is easy-computer"
    (should (player/is-ai? :easy-computer)))

  (it "returns true if player is hard-computer"
    (should (player/is-ai? :hard-computer)))

  (it "returns true if player is medium-computer"
    (should (player/is-ai? :medium-computer)))

  (it "returns false if player is not ai"
    (should-not (player/is-ai? :human))))

(describe "started-game?"
  (it "returns true if player :start-game is :first"
    (should (player/started-game? :first)))
  
  (it "returns false if player :start-game is not :first"
    (should-not (player/started-game? :second))))

(describe "define-player"
  (it "returns an instance of Player"
    (should (instance? Player
                       (player/define-player {:marker "x" :role "h"} :green :first))))

  (it "returns player with role ':human' and its marker"
    (should= :human
             (:role (player/define-player {:marker "x" :role "h"} :green :first))))

  (it "returns player with role ':easy-computer' and its marker"
    (should= :easy-computer
             (:role (player/define-player {:marker "o" :role "ec"} :pink :second))))

  (it "returns player with role ':hard-computer' and its marker"
    (should= :hard-computer
             (:role (player/define-player {:marker "h" :role "hc"} :yellow :first)))))
