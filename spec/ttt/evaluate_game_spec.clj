(ns ttt.evaluate-game-spec
  (:require [speclj.core :refer :all]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.board :as board]
            [ttt.player :as player]))

(describe "winner-marker"

  (with _ board/empty-spot)
  (with empty-board (board/new-board))

  (it "returns nil if board is empty"
    (should (nil? (evaluate-game/winner-marker @empty-board))))

  (it "returns nil if there is no winner"
    (should (nil? (evaluate-game/winner-marker [:x :o :x
                                                :o :x :o
                                                :o :x :o]))))

  (it "returns the winner marker if there is one on rows"
    (should= :x (evaluate-game/winner-marker [:x :x :x
                                              @_ @_ :o
                                              :o :o @_])))

  (it "returns the winner marker if there is one in second row"
    (should= :x (evaluate-game/winner-marker [@_ @_ :o
                                              :x :x :x
                                              :o :o @_])))

  (it "returns the winner marker if there is one in column"
    (should= :x (evaluate-game/winner-marker [:x @_ @_
                                              :x :o :o
                                              :x @_ @_])))

  (it "returns the winner marker if there is one in the second column"
    (should= :x (evaluate-game/winner-marker [@_ :x @_
                                              :o :x :o
                                              :o :x @_])))

  (it "returns the winner marker if there is one in a diagonal"
    (should= :o (evaluate-game/winner-marker [:o :x :x
                                              :x :o @_
                                              @_ @_ :o])))

  (it "returns the winner marker if there is one in the other diagonal"
    (should= :x (evaluate-game/winner-marker [:x @_ :x
                                              :o :x :o
                                              :x :o @_]))))

(describe "winner-role"

  (with _ board/empty-spot)

  (it "returns :human if :human won the game"
    (should= :human (evaluate-game/winner-role [:x :x :x
                                                :e :e @_
                                                :e :e @_]
                                                {:marker {:symbol :x :color :green} :role :human}
                                                {:marker {:symbol :e :color :blue} :role :easy-computer})))

  (it "should not return :easy-computer if :human won the game"
    (should-not (= :easy-computer
                   (evaluate-game/winner-role [:x :x :x
                                               :e :e @_
                                               :e :e @_]
                                               {:role :easy-computer :marker {:symbol :e :color :blue}}
                                               {:role :human :marker {:symbol :x :color :blue}}))))

  (it "returns :easy-computer if it won the game"
    (should= :easy-computer
             (evaluate-game/winner-role [:e :x :x
                                         :e @_ :x
                                         :e :x @_]
                                         {:role :easy-computer :marker {:symbol :e :color :blue}}
                                         {:role :human :marker {:symbol :x :color :blue}})))

  (it "returns :hard-computer if it won the game"
    (should= :hard-computer (evaluate-game/winner-role [:h :h :h
                                                        :e :e @_
                                                        @_ @_ @_]
                                                        {:role :hard-computer :marker {:symbol :h :color :blue}}
                                                        {:role :easy-computer :marker {:symbol :e :color :blue}}))))

(describe "is-winner-ai?"
  (with _ board/empty-spot)
  (it "returns false if winner has role :human"
    (should-not (evaluate-game/is-winner-ai? [:x :e :x
                                              :e :x :e
                                              :e :e :x]
                                              {:marker {:symbol :x :color :blue} :role :human}
                                              {:role :easy-computer :marker {:symbol :e :color :blue}})))
  (it "returns true if winner has role :easy-computer"
    (should (evaluate-game/is-winner-ai? [:x :x :e
                                          :e :e :e
                                          :e :e :x]
                                          {:marker {:symbol :x :color :blue} :role :human}
                                          {:role :easy-computer :marker {:symbol :e :color :blue}})))
  (it "returns true if winner has role :hard-computer"
    (should (evaluate-game/is-winner-ai? [:x :x :h
                                          :h :h :h
                                          :h :h :x]
                                          {:marker {:symbol :x :color :blue} :role :human}
                                          {:role :easy-computer :marker {:symbol :e :color :blue}}))))

(describe "draw?"
  (with _ board/empty-spot)
  (with empty-board (board/new-board))
  (it "returns false if board is empty"
    (should-not (evaluate-game/draw? @empty-board)))
  (it "returns false if board is not full"
    (should-not (evaluate-game/draw? [:x :x :x :x :x :x :x :x @_])))
  (it "returns false if board is full and there is a winner"
    (should-not (evaluate-game/draw? [:x :o :x
                                      :o :x :o
                                      :o :o :x])))
  (it "returns true if board is full and there is no winner"
    (should (evaluate-game/draw? [:x :o :x
                                  :o :x :o
                                  :o :x :o]))))

(describe "game-over?"
  (with _ board/empty-spot)
  (with empty-board (board/new-board))
  (it "returns false if board is empty"
    (should-not (evaluate-game/game-over? @empty-board)))
  (it "returns false if only some spots are taken"
    (should-not (evaluate-game/game-over? [:x :o @_ @_ @_ :x @_ @_ :o])))
  (it "returns true if there is a draw"
    (should (evaluate-game/game-over? [:x :o :x
                                       :o :x :o
                                       :o :x :o])))
  (it "returns true if there is a winner"
    (should (evaluate-game/game-over? [:x :x :x
                                       :o  @_ :o
                                       :o :x :o]))))
