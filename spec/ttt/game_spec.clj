(ns ttt.game-spec
  (require [speclj.core :refer :all]
           [ttt.game :refer :all]))

; (describe "won"
;   (it "finds combo with winning combination"
;     (should= '(0 1 2) (won [:x :x :x :_ :_ :o :_ :o :_])))
;   (it "returns nil if there is no winning combo"
;     (should (nil? (won [:_ :x :_ :o :_ :_ :x :_ :_])))))
