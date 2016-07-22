(ns ttt.rules-spec
  (:require [speclj.core :refer :all]
            [ttt.rules :refer :all]
            [ttt.board :as board]
            [ttt.player :as player]))

(describe "winner-player/marker"
  (with _ board/empty-spot)
  (with empty-board (board/new-board))
  (it "returns nil if board is empty"
    (should (nil? (winner-marker @empty-board))))
  (it "returns nil if there is no winner"
    (should (nil? (winner-marker [:x :o :x
                                  :o :x :o
                                  :o :x :o]))))
  (it "returns the winner player/marker if there is one on rows"
    (should= :x (winner-marker [:x :x :x
                                @_ @_ :o
                               :o :o @_])))
  (it "returns the winner player/marker if there is one in second row"
    (should= :x (winner-marker [@_ @_ :o
                                :x :x :x
                                :o :o @_])))
  (it "returns the winner player/marker if there is one in column"
    (should= :x (winner-marker [:x @_ @_
                                :x :o :o
                                :x @_ @_])))
  (it "returns the winner player/marker if there is one in the second column"
    (should= :x (winner-marker [@_ :x @_
                                :o :x :o
                                :o :x @_])))
  (it "returns the winner player/marker if there is one in a diagonal"
    (should= :o (winner-marker [:o :x :x
                                :x :o @_
                                @_ @_ :o])))
  (it "returns the winner player/marker if there is one in the other diagonal"
    (should= :x (winner-marker [:x @_ :x
                                :o :x :o
                                :x :o @_]))))

(describe "winner-player"
  (with human (player/make-player { :marker :x :role :human }))
  (with easy-computer (player/make-player { :role :easy-computer :marker :e }))
  (with hard-computer (player/make-player { :role :hard-computer :marker :h }))
  (with _ board/empty-spot)
    (it "returns human player if human won the game"
      (should= @human (winner-player [:x :x :x
                                      :e :e @_
                                      :e :e @_]
                                      @easy-computer
                                      @human)))
    (it "should not return easy-computer player if human won the game"
      (should-not (= @easy-computer (winner-player [:x :x :x
                                                    :e :e @_
                                                    :e :e @_]
                                                    @easy-computer
                                                    @human))))
    (it "returns easy-computer player if it won the game"
      (should= @easy-computer  (winner-player [:e :x :x
                                               :e @_ :x
                                               :e :x @_]
                                               @easy-computer
                                               @human)))
    (it "returns hard-computer player if it won the game"
      (should= @hard-computer (winner-player [:h :h :h
                                              :e :e @_
                                              @_ @_ @_]
                                              @hard-computer
                                              @easy-computer))))

(describe "is-winner-ai?"
  (with human (player/make-player { :marker :x :role :human }))
  (with easy-computer (player/make-player { :role :easy-computer :marker :e }))
  (with hard-computer (player/make-player { :role :hard-computer :marker :h }))
  (with _ board/empty-spot)
  (it "returns false if winner has :ai attribute false"
    (should-not (is-winner-ai? [:x :e :x
                                :e :x :e
                                :e :e :x]
                                @human
                                @easy-computer)))
  (it "returns true if winner is easy-computer"
    (should (is-winner-ai? [:x :x :e
                            :e :e :e
                            :e :e :x]
                            @human
                            @easy-computer)))
  (it "returns true if winner is hard-computer"
    (should (is-winner-ai? [:x :x :h
                            :h :h :h
                            :h :h :x]
                            @human
                            @hard-computer))))

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
