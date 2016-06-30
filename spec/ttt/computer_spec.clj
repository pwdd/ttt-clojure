(ns ttt.computer-spec
  (:require [speclj.core :refer :all]
            [ttt.computer :refer :all]
            [ttt.player :refer [make-player]]))

(def board-t )

(def computer (make-player { :marker :o :role :easy-computer }))
(def human (make-player { :marker :x :role :human }))

(describe "computer-spot"
  (it "returns an integer from 0 to board-length exclusive"
    (should (and (>= (computer-spot 9) 0)
                 (< (computer-spot 9) 9)))))


(describe "best-move"
  (it "returns spot that blocks opponent victory"
    (should= 8 (best-move [:x :o :o
                         :o :x :x
                         :_ :_ :_]
                         computer
                         human
                         0)))
  (it "returns spot makes computer win instead of blocking opponent"
    (should= 0 (best-move [:_ :o :o
                         :_ :x :x
                         :x :x :o]
                         computer
                         human
                         0)))
  (it "avoids opponent to create an invincible situation"
    (should (or (= 2 (best-move [:o :_ :_
                                 :_ :x :_
                                 :_ :_ :x]
                                 computer
                                 human
                                 0))
                (= 6 (best-move [:o :_ :_
                                 :_ :x :_
                                 :_ :_ :x]
                                 computer
                                 human
                                 0))))))
