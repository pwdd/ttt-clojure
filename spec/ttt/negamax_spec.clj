(ns ttt.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt.negamax :refer :all]
            [ttt.player :refer [make-player]]
            [ttt.game :refer [create-game]]))

(def easy-computer (make-player { :marker :e :role :easy-computer }))
(def human (make-player { :marker :x :role :human }))
(def hard-computer (make-player { :marker :o :role :hard-computer }))
(def hard-computer-one (make-player { :marker :x :role :hard-computer }))

(def human-hard (create-game human hard-computer))
(def hard-hard (create-game hard-computer-one hard-computer))

(describe "scores"
  (it "test"
    (should= 1 (scores human-hard
                          [:o :_ :o
                           :_ :x :x
                           :_ :_ :x]
                           hard-computer
                           human
                           0))))

(describe "board-analysis :hard-x-hard"
  (it "returns 0 if there is not winner"
    (should (zero? (board-analysis hard-hard
                                   [:_ :x :_
                                    :o :_ :_
                                    :_ :_ :_]
                                    hard-computer
                                    hard-computer-one
                                    2))))
  (it "returns board value when the first player won"
    (should= 12 (board-analysis hard-hard
                                [:x :x :x
                                 :o :o :_
                                 :_ :_ :_]
                                 hard-computer-one
                                 hard-computer
                                 2)))
  (it "returns board value when the second player won"
    (should= 12 (board-analysis human-hard
                                 [:x :o :_
                                  :o :x :_
                                  :_ :_ :x]
                                  hard-computer-one
                                  hard-computer
                                  2)))
  (it "returns board value when second-player won"
    (should= 13 (board-analysis human-hard
                                [:o :o :o
                                 :x :x :_
                                 :X :_ :_]
                                 hard-computer-one
                                 hard-computer
                                 3))))

(describe "board-analysis :default"
  (it "returns 0 if there is not winner"
    (should (zero? (board-analysis human-hard
                                   [:_ :x :_
                                    :o :_ :_
                                    :_ :_ :_]
                                    human
                                    hard-computer-one
                                    2))))
  (it "returns board value when human won"
    (should= -8 (board-analysis human-hard
                                [:x :x :x
                                 :o :o :_
                                 :_ :_ :_]
                                 human
                                 hard-computer
                                 2)))
  (it "returns board value when easy won"
    (should= -8 (board-analysis human-hard
                                 [:e :o :_
                                  :o :e :_
                                  :_ :_ :e]
                                  easy-computer
                                  hard-computer
                                  2)))
  (it "returns board value when hard-computer won"
    (should= 13 (board-analysis human-hard
                                [:o :o :o
                                 :x :x :_
                                 :X :_ :_]
                                 human
                                 hard-computer
                                 3))))

(describe "best-move"
  (it "returns spot that blocks opponent victory"
    (should= 8 (best-move human-hard
                          [:x :o :o
                           :o :x :x
                           :_ :_ :_]
                           hard-computer
                           human
                           0)))
  (it "returns spot that makes computer win instead of blocking opponent"
    (should= 0 (best-move human-hard
                          [:_ :o :o
                           :_ :x :x
                           :x :x :o]
                           hard-computer
                           human
                           0)))
  (it "avoids opponent to create an invincible situation"
    (should (or (= 2 (best-move human-hard
                                [:o :_ :_
                                 :_ :x :_
                                 :_ :_ :x]
                                 hard-computer
                                 human
                                 0))
                (= 6 (best-move human-hard
                                [:o :_ :_
                                 :_ :x :_
                                 :_ :_ :x]
                                 hard-computer
                                 human
                                 0)))))
  (it "blocks opponent from winning"
    (should= 6 (best-move human-hard
                          [:o :x :x
                           :_ :x :_
                           :_ :o :_]
                           hard-computer
                           human
                           0))))
