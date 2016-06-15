(ns ttt.computer-spec
  (:require [speclj.core :refer :all]
            [ttt.computer :refer :all]))

(describe "computer-spot"
  (it "returns an integer from 0 to board-length exclusive"
    (should (and (>= (computer-spot 9) 0)
                 (< (computer-spot 9) 9)))))
