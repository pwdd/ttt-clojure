(ns ttt.game-spec
  (require [speclj.core :refer :all]
           [ttt.game :refer :all]))

(describe "valid-selection"
  (it "accepts only 'h' as valid input"
    (should (valid-selection? "h"))))

(describe "who-plays"
  (it "returns 'h' if human starts the game"
    (should= "h" (with-in-str "h" (who-plays))))
  (it "returns 'h' if user spells out 'human'"
    (should= "h" (with-in-str "hUmAn" (who-plays))))
  (it "returns 'c' if computer starts the game"
    (should= "c" (with-in-str "c" (who-plays))))
  (it "returns 'c' if user spells 'computer'"
    (should= "c" (with-in-str "coMpUteR" (who-plays)))))

(describe "player-spot"
  (it "gets user spot"
    (should= 3 (with-in-str "4" (player-spot {:type "human" :marker :x}))))
  (it "gets computer spot"
    (should (integer? (player-spot {:type "computer" :marker :o})))))

(describe "game-type"
  (it "returns :human-computer if game is h vs c"
    (should= :human-computer (game-type "human" "computer")))
  (it "returns :human-computer if game is c vs h"
    (should= :human-computer (game-type "computer" "human")))
  (it "returns nil if c vs c"
    (should-not (game-type "computer" "computer")))
  (it "returns nil if h vs h"
    (should-not (game-type "human" "human"))))
