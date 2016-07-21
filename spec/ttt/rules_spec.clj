(ns ttt.rules-spec
  (:require [speclj.core :refer :all]
            [ttt.rules :refer :all]
            [ttt.board :as board]))

(describe "draw?"
  (let [_ board/empty-spot
        empty-board (board/new-board)]
    (it "returns false if board is empty"
      (should-not (draw? empty-board)))
    (it "returns false if board is not full"
      (should-not (draw? [:x :x :x :x :x :x :x :x _])))
    (it "returns false if board is full and there is a winner"
      (should-not (draw? [:x :o :x
                          :o :x :o
                          :o :o :x])))
    (it "returns true if board is full and there is no winner"
      (should (draw? [:x :o :x
                      :o :x :o
                      :o :x :o])))))

(describe "game-over?"
  (let [_ board/empty-spot
        empty-board (board/new-board)]
    (it "returns false if board is empty"
      (should-not (game-over? empty-board)))
    (it "returns false if only some spots are taken"
      (should-not (game-over? [:x :o _ _ _ :x _ _ :o])))
    (it "returns true if there is a draw"
      (should (game-over? [:x :o :x
                           :o :x :o
                           :o :x :o])))
    (it "returns true if there is a winner"
      (should (game-over? [:x :x :x
                           :o  _ :o
                           :o :x :o])))))
