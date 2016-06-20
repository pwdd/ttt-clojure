(ns ttt.computer-spec
  (:require [speclj.core :refer :all]
            [ttt.computer :refer :all]))

(describe "get-computer-spot"
  (it "returns a number from 0 to board-length exclusive"
    (should (and (< (get-computer-spot 9) 9)
                 (>= (get-computer-spot 9) 0)))))
