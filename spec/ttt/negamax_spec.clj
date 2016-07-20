(ns ttt.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt.negamax :refer :all]
            [ttt.player :as player]
            [ttt.game :as game]))

; (describe "board-value"
;   (let [easy-computer (player/make-player { :marker :e :role :easy-computer })
;         human (player/make-player { :marker :x :role :human })
;         hard-computer (player/make-player { :marker :o :role :hard-computer })]
;   (context ":human"
;     (it "returns depth - 10 if player is human"
;       (should= -8 (board-value human 2))))
;   (context ":easy-computer"
;     (it "returns depth - 10 if player is easy computer"
;       (should= -8 (board-value easy-computer 2))))
;   (context ":hard-computer"
;     (it "returns depth + 10 if player is hard-computer"
;       (should= 8 (board-value hard-computer 2))))))

(describe "board-analysis :hard-x-hard"
  (let [hard-computer (player/make-player { :marker :o :role :hard-computer })
        hard-computer-one (player/make-player { :marker :x :role :hard-computer })
        hard-hard (game/create-game :hard-computer-one :hard-computer)]
  (it "returns 0 if there is not winner"
    (should (zero? (board-analysis hard-hard
                                   [:_ :x :_
                                    :o :_ :_
                                    :_ :_ :_]
                                    hard-computer
                                    hard-computer-one
                                    2))))
  (it "returns board value when the first player won"
    (should= 8 (board-analysis hard-hard
                                [:x :x :x
                                 :o :o :_
                                 :_ :_ :_]
                                 hard-computer-one
                                 hard-computer
                                 2)))
  (it "returns board value when the opponent player won"
    (should= -8 (board-analysis hard-hard
                                 [:o :x :_
                                  :x :o :_
                                  :_ :_ :o]
                                  hard-computer-one
                                  hard-computer
                                  2)))))

(describe "board-analysis :default"
  (let [human (player/make-player { :marker :x :role :human })
        easy-computer (player/make-player { :marker :o :role :easy-computer })
        hard-computer (player/make-player { :marker :o :role :hard-computer })
        hard-computer-one (player/make-player { :marker :x :role :hard-computer })
        human-hard (game/create-game :human :hard-computer)
        easy-hard (game/create-game :hard-computer :easy-computer)]
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
    (should= -8 (board-analysis easy-hard
                                 [:e :o :_
                                  :o :e :_
                                  :_ :_ :e]
                                  easy-computer
                                  hard-computer
                                  2)))
  (it "returns board value when hard-computer won"
    (should= 7 (board-analysis human-hard
                                [:o :o :o
                                 :x :x :_
                                 :X :_ :_]
                                 human
                                 hard-computer
                                 3)))))

(describe "best-move"
  (let [human (player/make-player { :marker :x :role :human })
        hard-computer (player/make-player { :marker :o :role :hard-computer })
        human-hard (game/create-game :human :hard-computer)]
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
                           0)))))
