(ns ttt.computer-spec
  (:require [speclj.core :refer :all]
            [ttt.computer :refer :all]
            [ttt.player :refer :all])
  (:import [ttt.player Player]))

(def board-t )

(def computer (->Player :o :easy-computer true 1))
(def human (->Player :x :human false -1))

(describe "computer-spot"
  (it "returns an integer from 0 to board-length exclusive"
    (should (and (>= (computer-spot 9) 0)
                 (< (computer-spot 9) 9)))))

(describe "negamax"
  (it "returns -1 if opponent won"
    (should= -1 (negamax [:x :o :o
                         :o :x :x
                         :o :x :x]
                         computer
                         human)))
  (it "returns 1 if computer won"
    (should= 1 (negamax [:o :o :o
                         :x :o :x
                         :o :x :x]
                         computer
                         human)))
  (it "returns spot that blocks opponent victory"
    (should= 8 (negamax [:x :o :o
                         :o :x :x
                         :_ :_ :_]
                         computer
                         human)))
  (it "returns spot makes computer win instead of blocking opponent"
    (should= 0 (negamax [:_ :o :o
                         :_ :x :x
                         :x :x :o]
                         computer
                         human)))
  (it "avoids opponent to create an invincible situation"
    (should= -10 (negamax [:o :_ :_
                           :_ :x :_
                           :_ :_ :x]
                           computer
                           human))))
