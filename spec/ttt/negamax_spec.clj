(ns ttt.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt.negamax :as negamax]
            [ttt.board :as board]))

(describe "board-analysis"

  (with x {:symbol :x :color :blue})
  (with o {:symbol :o :color :red})

  (it "returns 10 if current player won and -10 if opponent won"
    (should= 10 (negamax/board-analysis [@x @x @x
                                         @o @o @x
                                         @o @x @o]
                                         @x
                                         @o
                                         negamax/start-depth))
    (should= -10 (negamax/board-analysis [@x @x @x
                                          @o @o @x
                                          @o @x @o]
                                          @o
                                          @x
                                          negamax/start-depth)))

  (it "returns 8 if current-player won and -8 if opponent won"
    (should= 8 (negamax/board-analysis [@x @x @x
                                        @o @o @x
                                        @o @x @o]
                                        @x
                                        @o
                                        2))
    (should= -8 (negamax/board-analysis [@x @x @x
                                         @o @o @x
                                         @o @x @o]
                                         @o
                                         @x
                                         2)))

  (it "returns 0 if game ended in a draw"
    (should= 0 (negamax/board-analysis [@x @x @o
                                        @o @o @x
                                        @x @o @x]
                                        @x
                                        @o
                                        2))))

(describe "negamax-score"

  (with _ board/empty-spot)
  (with x {:symbol :x :color :blue})
  (with o {:symbol :o :color :red})

  (it "returns 0 if game will end in a tie"
    (should= 0 (negamax/negamax-score [@x @x @o
                                       @o @o @_
                                       @x @x @o]
                                       @x
                                       @o
                                       2
                                       -100
                                       100)))

  (it "returns 9 if current player will win the game"
    (should= 9 (negamax/negamax-score [@x @x @_
                                       @o @o @x
                                       @o @x @o]
                                       @x
                                       @o
                                       negamax/start-depth
                                       -100
                                       100)))

  (it "returns -8 if opponent will win the game"
    (should= -8 (negamax/negamax-score [@o @x @o
                                        @o @o @x
                                        @_ @x @_]
                                        @x
                                        @o
                                        negamax/start-depth
                                        -100
                                        100))))
