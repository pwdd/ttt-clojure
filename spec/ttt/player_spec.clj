(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :as player]
            [ttt.board :as board])
  (:import [ttt.player Player]))

(describe "is-ai?"
  (it "returns true if player is easy-computer"
    (should (player/is-ai? :easy-computer)))

  (it "returns true if player is hard-computer"
    (should (player/is-ai? :hard-computer)))

  (it "returns false if player is not ai"
    (should-not (player/is-ai? :human))))

(describe "marker"
  (it "returns the marker from a player record"
    (should= {:token :x :color :green}
             (player/marker {:marker {:token :x :color :green}
                             :role :human}))))

(describe "color"
  (it "returns the color of a marker from a player record"
    (should= :green (player/color {:marker {:token :x :color :green}
                                   :role :human}))))

(describe "define-player"
  (it "returns an instance of Player"
    (should (instance? Player
                       (player/define-player {:marker "x" :role "h"} :green))))

  (it "returns player with role ':human' and its marker"
    (should= :human
             (:role (player/define-player {:marker "x" :role "h"} :green))))

  (it "returns player with role ':easy-computer' and its marker"
    (should= :easy-computer
             (:role (player/define-player {:marker "o" :role "ec"} :pink))))

  (it "returns player with role ':hard-computer' and its marker"
    (should= :hard-computer
             (:role (player/define-player {:marker "h" :role "hc"} :yellow)))))
