(ns ttt.game-spec
  (require [speclj.core :refer :all]
           [ttt.game :refer :all]))

(describe "valid-selection"
  (let [acceptable ["h" "c" "human" "computer"]]
    (it "only accepts 'h' 'c' 'human' 'computer' as valid"
      (should (every? valid-selection? acceptable))))
  (it "does not accept any other string"
    (should-not (valid-selection? "a"))))

(describe "define-player"
  (it "returns player with type 'human' and its marker"
    (should= {:type :human :marker :x}
             (with-in-str "h" (define-player :x))))
  (it "returns player with type 'computer' and its marker"
    (should= {:type :computer :marker :x}
             (with-in-str "c" (define-player :x)))))

(describe "player-spot"
  (it "gets user spot"
    (should= 3 (with-in-str "4" (player-spot {:type :human :marker :x}))))
  (it "gets computer spot"
    (should (integer? (player-spot {:type :computer :marker :o})))))

(describe "game-type"
  (it "returns :human-computer if game is h vs c"
    (should= :human-computer (game-type :human :computer)))
  (it "returns :human-computer if game is c vs h"
    (should= :human-computer (game-type :computer :human)))
  (it "returns nil if c vs c"
    (should-not (game-type :computer :computer)))
  (it "returns nil if h vs h"
    (should-not (game-type :human :human))))
