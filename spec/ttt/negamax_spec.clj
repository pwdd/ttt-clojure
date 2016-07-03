(ns ttt.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt.negamax :refer :all]
            [ttt.player :refer [make-player]]))

(def easy-computer (make-player { :marker :o :role :easy-computer }))
(def human (make-player { :marker :x :role :human }))
(def hard-computer (make-player { :marker :x :role :hard-computer }))

(describe "game-type"
  (it "returns :human-computer if game is human vs computer"
    (should= :human-computer (game-type human easy-computer)))
  (it "returns :human-computer if game is computer vs human"
    (should= :human-computer (game-type hard-computer human)))
  (it "returns nil if game is computer vs computer"
    (should-not (game-type easy-computer easy-computer)))
  (it "returns nil if game is human vs human"
    (should-not (game-type human human))))

(describe "board-analysis"
  (it "returns board value when human won"
    (should= -8 (board-analysis [:x :x :x
                                 :o :o :_
                                 :_ :_ :_]
                                 human
                                 easy-computer
                                 2)))
  (it "returns board value when computer won"
    (should= 13 (board-analysis [:o :o :o
                                 :x :x :_
                                 :_ :_ :_]
                                 human
                                 easy-computer
                                 3))))

(describe "computer-spot"
  (it "returns an integer from 0 to board-length exclusive"
    (should (and (>= (computer-spot 9) 0)
                 (< (computer-spot 9) 9)))))

(describe "best-move"
  (it "returns spot that blocks opponent victory"
    (should= 8 (best-move [:x :o :o
                           :o :x :x
                           :_ :_ :_]
                           easy-computer
                           human
                           0)))
  (it "returns spot that makes computer win instead of blocking opponent"
    (should= 0 (best-move [:_ :o :o
                           :_ :x :x
                           :x :x :o]
                           easy-computer
                           human
                           0)))
  (it "returns winning spot"
    (should= 1 (best-move [:o :_ :o
                           :_ :x :x
                           :_ :_ :x]
                           easy-computer
                           human
                           0)))
  (it "avoids opponent to create an invincible situation"
    (should (or (= 2 (best-move [:o :_ :_
                                 :_ :x :_
                                 :_ :_ :x]
                                 easy-computer
                                 human
                                 0))
                (= 6 (best-move [:o :_ :_
                                 :_ :x :_
                                 :_ :_ :x]
                                 easy-computer
                                 human
                                 0)))))
  (it "blocks opponent from winning"
    (should= 6 (best-move [:o :x :x
                           :_ :x :_
                           :_ :o :_]
                           easy-computer
                           human
                           0))))
