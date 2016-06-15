(ns ttt.game-spec
  (require [speclj.core :refer :all]
           [ttt.game :refer :all]))

(describe "first-player"
  (it "holds first player marker"
    (should-not (nil? first-player))))

(describe "second-player"
  (it "holds second player marker"
    (should-not (nil? second-player))))
