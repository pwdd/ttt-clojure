(ns ttt.game-spec
  (require [speclj.core :refer :all]
           [ttt.game :refer :all]))

(describe "first-player"
  (it "holds first player marker"
    (should= (first-player :marker) :x))
  (it "holds first player type"
    (should= (first-player :type) "human")))

(describe "second-player"
  (it "holds second player marker"
    (should= (second-player :marker) :o))
  (it "holds second player type"
    (should= (second-player :type) "computer")))

(describe "player-spot"
  (it "gets user spot"
    (should= 3 (with-in-str "4" (player-spot {:type "human" :marker :x}))))
  (it "gets computer spot"
    (should (integer? (player-spot {:type "computer" :marker :o})))))
