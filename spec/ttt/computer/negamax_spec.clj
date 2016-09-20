(ns ttt.computer.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt.computer.negamax :as negamax]
            [ttt.board :as board]))

(describe "board-analysis"

  (with x {:symbol :x :color :blue})
  (with o {:symbol :o :color :red})

  (it "returns 100 if current player won and -100 if opponent won"
    (should= 100 (negamax/board-analysis [@x @x @x
                                          @o @o @x
                                          @o @x @o]
                                          @x
                                          @o
                                          negamax/start-depth))
    (should= -100 (negamax/board-analysis [@x @x @x
                                           @o @o @x
                                           @o @x @o]
                                           @o
                                           @x
                                           negamax/start-depth)))

  (it "returns 80 if current-player won and -8 if opponent won"
    (should= 98 (negamax/board-analysis [@x @x @x
                                         @o @o @x
                                         @o @x @o]
                                         @x
                                         @o
                                         2))
    (should= -98 (negamax/board-analysis [@x @x @x
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
                                       2)))

  (it "returns 9 if current player will win the game"
    (should= 99 (negamax/negamax-score [@x @x @_
                                       @o @o @x
                                       @o @x @o]
                                       @x
                                       @o
                                       negamax/start-depth)))

  (it "returns -8 if opponent will win the game"
    (should= -98 (negamax/negamax-score [@o @x @o
                                        @o @o @x
                                        @_ @x @_]
                                        @x
                                        @o
                                        negamax/start-depth))))
