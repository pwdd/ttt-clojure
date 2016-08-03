(ns ttt.evaluate-game-spec
  (:require [speclj.core :refer :all]
            [ttt.evaluate-game :refer :all]
            [ttt.board :as board]
            [ttt.player :as player]))

(describe "winner-marker"

  (with _ board/empty-spot)
  (with empty-board (board/new-board))

  (it "returns nil if board is empty"
    (should (nil? (winner-marker @empty-board))))

  (it "returns nil if there is no winner"
    (should (nil? (winner-marker [:x :o :x
                                  :o :x :o
                                  :o :x :o]))))

  (it "returns the winner marker if there is one on rows"
    (should= :x (winner-marker [:x :x :x
                                @_ @_ :o
                                :o :o @_])))

  (it "returns the winner marker if there is one in second row"
    (should= :x (winner-marker [@_ @_ :o
                                :x :x :x
                                :o :o @_])))

  (it "returns the winner marker if there is one in column"
    (should= :x (winner-marker [:x @_ @_
                                :x :o :o
                                :x @_ @_])))

  (it "returns the winner marker if there is one in the second column"
    (should= :x (winner-marker [@_ :x @_
                                :o :x :o
                                :o :x @_])))

  (it "returns the winner marker if there is one in a diagonal"
    (should= :o (winner-marker [:o :x :x
                                :x :o @_
                                @_ @_ :o])))

  (it "returns the winner marker if there is one in the other diagonal"
    (should= :x (winner-marker [:x @_ :x
                                :o :x :o
                                :x :o @_]))))

(describe "winner-role"

  (with _ board/empty-spot)

  (it "returns :human if :human won the game"
    (should= :human (winner-role [:x :x :x
                                  :e :e @_
                                  :e :e @_]
                                  {:marker :x :role :human}
                                  {:marker :e :role :easy-computer})))

  (it "should not return :easy-computer if :human won the game"
    (should-not (= :easy-computer (winner-role [:x :x :x
                                                :e :e @_
                                                :e :e @_]
                                                {:role :easy-computer :marker :e}
                                                {:role :human :marker :x}))))

  (it "returns :easy-computer if it won the game"
    (should= :easy-computer  (winner-role [:e :x :x
                                           :e @_ :x
                                           :e :x @_]
                                           {:role :easy-computer :marker :e}
                                           {:role :human :marker :x})))

  (it "returns :hard-computer if it won the game"
    (should= :hard-computer (winner-role [:h :h :h
                                          :e :e @_
                                          @_ @_ @_]
                                          {:role :hard-computer :marker :h}
                                          {:role :easy-computer :marker :e}))))

(describe "is-winner-ai?"
  (with _ board/empty-spot)
  (it "returns false if winner has :ai attribute false"
    (should-not (is-winner-ai? [:x :e :x
                                :e :x :e
                                :e :e :x]
                                {:marker :x :role :human}
                                {:role :easy-computer :marker :e})))
  (it "returns true if winner is easy-computer"
    (should (is-winner-ai? [:x :x :e
                            :e :e :e
                            :e :e :x]
                            {:marker :x :role :human}
                            {:role :easy-computer :marker :e})))
  (it "returns true if winner is hard-computer"
    (should (is-winner-ai? [:x :x :h
                            :h :h :h
                            :h :h :x]
                            {:marker :x :role :human}
                            {:role :easy-computer :marker :e}))))

(describe "draw?"
  (with _ board/empty-spot)
  (with empty-board (board/new-board))
  (it "returns false if board is empty"
    (should-not (draw? @empty-board)))
  (it "returns false if board is not full"
    (should-not (draw? [:x :x :x :x :x :x :x :x @_])))
  (it "returns false if board is full and there is a winner"
    (should-not (draw? [:x :o :x
                        :o :x :o
                        :o :o :x])))
  (it "returns true if board is full and there is no winner"
    (should (draw? [:x :o :x
                    :o :x :o
                    :o :x :o]))))

(describe "game-over?"
  (with _ board/empty-spot)
  (with empty-board (board/new-board))
  (it "returns false if board is empty"
    (should-not (game-over? @empty-board)))
  (it "returns false if only some spots are taken"
    (should-not (game-over? [:x :o @_ @_ @_ :x @_ @_ :o])))
  (it "returns true if there is a draw"
    (should (game-over? [:x :o :x
                         :o :x :o
                         :o :x :o])))
  (it "returns true if there is a winner"
    (should (game-over? [:x :x :x
                         :o  @_ :o
                         :o :x :o]))))
