(ns ttt.game-spec
  (require [speclj.core :refer :all]
           [ttt.game :refer :all]))

(describe "player"
  (it "holds information if player is human"
    (should= "human" ((player "human" :x) :type)))
  (it "holds the human marker"
    (should= :x ((player "human" :x) :marker)))
  (it "holds information if player is computer"
    (should= "computer" ((player "computer" :o) :type)))
  (it "holds the computer marker"
    (should= :o ((player "computer" :o) :marker))))


(describe "get-player-spot"
  (it "calls 'get-human-spot' if player type is 'human'"
    (should= )))
