(ns ttt.rules-spec
  (:require [speclj.core :refer :all]
            [ttt.rules :refer :all]
            [ttt.player :as player]
            [ttt.board :as board]))

(describe "draw?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        easy-computer (player/make-player { :role :easy-computer :marker :e })
        e (player/marker easy-computer)
        _ board/empty-spot
        empty-board (board/new-board)]
    (it "returns false if board is empty"
      (should-not (draw? empty-board)))
    (it "returns false if board is not full"
      (should-not (draw? [u u u u u u u u _])))
    (it "returns false if board is full and there is a winner"
      (should-not (draw? [u e u
                          e u e
                          e e u])))
    (it "returns true if board is full and there is no winner"
      (should (draw? [u e u
                      e u e
                      e u e])))))

(describe "game-over?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        easy-computer (player/make-player { :role :easy-computer :marker :e })
        e (player/marker easy-computer)
        _ board/empty-spot
        empty-board (board/new-board)]
    (it "returns false if board is empty"
      (should-not (game-over? empty-board)))
    (it "returns false if only some spots are taken"
      (should-not (game-over? [u e _ _ _ u _ _ e])))
    (it "returns true if there is a draw"
      (should (game-over? [u e u
                           e u e
                           e u e])))
    (it "returns true if there is a winner"
      (should (game-over? [u u u
                           e _ e
                           e u e])))))
