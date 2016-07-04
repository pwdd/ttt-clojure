(ns ttt.spots-spec
  (:require [speclj.core :refer :all]
            [ttt.spots :refer :all]
            [ttt.player :refer [make-player]]))

(def human (make-player { :role :human :marker :x }))
(def easy-computer (make-player { :role :easy-computer :marker :o }))

(describe "select-spot :easy-computer"
  (it "returns an integer from 0 to board-length exclusive"
   (should (and (>= (select-spot easy-computer { :board-length 9 }) 0)
                (< (select-spot easy-computer { :board-length 9 }) 9)))))

(describe "select-spot :human"
  (it "returns 0 if input is '1'"
    (should= 0 (with-in-str "1" (select-spot human))))
  (it "returns 8 if input is 9"
    (should= 8 (with-in-str "9" (select-spot human)))))
