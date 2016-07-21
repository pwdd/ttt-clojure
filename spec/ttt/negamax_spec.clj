(ns ttt.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt.negamax :refer :all]
            [ttt.player :as player]
            [ttt.board :as board]
            [ttt.game :as game]))

(describe "board-analysis"
  (let [_ board/empty-spot
       hard-computer-one (player/make-player { :role :hard-computer :marker :x })
       hard-computer-two (player/make-player { :role :hard-computer :marker :o })
       human (player/make-player { :role :human :marker :o })
       easy-computer (player/make-player { :role :easy-computer :marker :o })]
    (it "returns 0 if there is no winner"
      (should (zero? (board-analysis [_ :x _
                                      :o _ _
                                       _ _ _]
                                       hard-computer-one
                                       hard-computer-two
                                       2))))
    (it "returns a positive value if current player won"
      (should= 8 (board-analysis [:x :x :x
                                  :o :o _
                                  _ _ _]
                                  hard-computer-one
                                  hard-computer-two
                                  2)))
    (it "returns a negative number if opponent won, even if opponent is hard-computer"
      (should= -8 (board-analysis [:x :x _
                                   :o :o :o
                                   _ :x _]
                                   hard-computer-one
                                   hard-computer-two
                                   2)))
    (it "returns a negative number if opponent won, and opponent is human"
      (should= -8 (board-analysis [:x :x :o
                                   _ :o _
                                   :o :x _]
                                   hard-computer-one
                                   human
                                   2)))
    (it "returns a negative number if opponent won, and opponent is easy-computer"
      (should= -8 (board-analysis [:o :x :x
                                   :o _ :x
                                   :o :x _]
                                  hard-computer-one
                                  easy-computer
                                  2)))))

(describe "best-move"
  (let [_ board/empty-spot
        human (player/make-player { :marker :x :role :human })
        hard-computer (player/make-player { :marker :o :role :hard-computer })
        human-hard (game/create-game :human :hard-computer)]
  (it "returns spot that blocks opponent victory"
    (should= 8 (best-move human-hard
                          [:x :o :o
                           :o :x :x
                           _ _ _]
                           hard-computer
                           human
                           start-depth)))
  (it "returns spot that makes computer win instead of blocking opponent"
    (should= 0 (best-move human-hard
                          [_ :o :o
                           _ :x :x
                           :x :x :o]
                           hard-computer
                           human
                           start-depth)))
  (it "avoids opponent to create an invincible situation"
    (should (or (= 2 (best-move human-hard
                                [:o _ _
                                 _ :x _
                                 _ _ :x]
                                 hard-computer
                                 human
                                 start-depth))
                (= 6 (best-move human-hard
                                [:o _ _
                                 _ :x _
                                 _ _ :x]
                                 hard-computer
                                 human
                                 start-depth)))))
  (it "blocks opponent from winning"
    (should= 6 (best-move human-hard
                          [:o :x :x
                           _ :x _
                           _ :o _]
                           hard-computer
                           human
                           start-depth)))))
