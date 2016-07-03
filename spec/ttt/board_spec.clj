(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]
            [ttt.player :refer [make-player marker]]))

(def human (make-player { :marker :x :role :human }))
(def easy-computer (make-player { :role :easy-computer :marker :e }))
(def hard-computer (make-player { :role :hard-computer :marker :h }))

(def u (marker human))
(def e (marker easy-computer))
(def h (marker hard-computer))
(def _ empty-spot)
(def empty-board (new-board))

(describe "board-size"
  (it "has a default size"
    (should= 3 board-size)))

(describe "board-length"
  (it "has the default value of squared board-size"
    (should= 9 board-length)))

(describe "winning-combos"
  (it "holds all winning combination"
    (should= 8 (count winning-combos))))

(describe "new-board"
  (it "is a vector of empty spots and with size equal to board length"
    (should (and (= board-length (count (new-board)))
                 (every? #{empty-spot} (new-board))))))

(describe "move"
  (it "sets a value to an empty board"
    (should= [u _ _ _ _ _ _ _ _]
             (move empty-board human 0)))
  (it "sets a spot on a board with some spots already taken"
    (should= [u _ _ e _ u _ _ _]
             (move [u _ _ e _ _ _ _ _] human 5))))

(describe "is-available?"
  (it "returns true when spot is not taken"
      (should (is-available? empty-board 0)))
   (it "returns false when spot is taken"
      (should-not (is-available? [u _ _ _ _ _ _ _ _] 0))))

(describe "is-full?"
  (it "returns true if board is full"
    (should (is-full? (vec (repeat board-length u)))))
  (it "returns false if board is empty"
    (should-not (is-full? empty-board)))
  (it "returns false if there is any spot available"
    (should-not (is-full? [u u u u u u u u _]))))

(describe "is-empty?"
  (it "returns true if board has only empty spots"
    (should (is-empty? empty-board)))
  (it "returns false if any spot is taken"
    (should-not (is-empty? [_ _ _ _ _ u _ _ _]))))

(describe "available-spots"
  (it "returns a list with one element if only one spot is available"
    (should= '(8) (available-spots [u u u u u u u u _])))
  (it "returns a vector with more than one spot available"
    (should= '(0 1 8) (available-spots [_ _ u u u u u u _])))
  (it "returns an empty vector if no spot is available"
    (should= '() (available-spots [(vec (repeat board-length u))]))))

(describe "is-valid-move?"
  (it "returns true if input is valid move"
    (should (is-valid-move? empty-board 0)))
  (it "returns true if is valid move on a board with spots taken"
    (should (is-valid-move? [u _ e _ _ _ _ _ _] 3)))
  (it "returns false if spot is taken"
    (should-not (is-valid-move? [_ u e _ _ _ _ _ _] 1)))
  (it "returns false if input is out of range"
    (should-not (is-valid-move? empty-board 10))))

(describe "repeated?"
  (it "returns false if board is empty"
    (should-not (repeated? empty-board [0 1 2])))
  (it "returns false if there is no repetition on given postions"
    (should-not (repeated? [u e u
                            e u e
                            e u u] [0 1 2])))
  (it "returns true if there are repeated elements in a row"
    (should (repeated? [u u u
                        e _ _
                        e e _] [0 1 2])))
  (it "returns true if there are repeated elements in a diagonal"
    (should (repeated? [e _ u
                        e u _
                        u e _] [2 4 6]))))

(describe "find-repetition"
  (it "returns an empty list if board is empty"
    (should= '()
             (find-repetition empty-board)))
  (it "returns an empty list if board is full and there is no winner"
    (should= '()
             (find-repetition [u e u
                               e u e
                               e u e])))
  (it "identifies a single combo with repeated markers"
    (should= '([0 1 2])
             (find-repetition [u u u
                               e _ _
                               e e _])))
  (it "identifies multiple combos with repeated markers"
    (should= '([3 4 5] [6 7 8])
              (find-repetition [u e _
                                e e e
                                u u u]))))

(describe "winning-combo"
  (it "returns nothing when board is empty"
    (should-not (winning-combo empty-board)))
  (it "returns nothing if finds 3 empty spots and there is no winner"
    (should-not (winning-combo [u e u
                                _ _ _
                                u _ e])))
  (it "returns winning row even if there are 3 empty spots in the board"
    (should= [0 1 2] (winning-combo [u u u
                                     _ _ _
                                     u _ e])))
  (it "returns winning row"
    (should= [0 1 2] (winning-combo [u u u
                                     e e _
                                     e _ _])))
  (it "returns winning column"
    (should= [1 4 7] (winning-combo [u e u
                                     _ e _
                                     u e u])))
  (it "returns winning diagonal"
    (should= [2 4 6] (winning-combo [u _ e
                                     _ e u
                                     e u u]))))

(describe "winner-marker"
  (it "returns nil if board is empty"
    (should (nil? (winner-marker empty-board))))
  (it "returns nil if there is no winner"
    (should (nil? (winner-marker [u e u
                                  e u e
                                  e u e]))))
  (it "returns the winner marker if there is one on rows"
    (should= u (winner-marker [u u u
                               _ _ e
                               e e _])))
  (it "returns the winner marker if there is one in second row"
    (should= u (winner-marker [_ _ e
                               u u u
                               e e _])))
  (it "returns the winner marker if there is one in column"
    (should= u (winner-marker [u _ _
                               u e e
                               u _ _])))
  (it "returns the winner marker if there is one in the second column"
    (should= u (winner-marker [_ u _
                               e u e
                               e u _])))
  (it "returns the winner marker if there is one in a diagonal"
    (should= e (winner-marker [e u u
                               u e _
                               _ _ e])))
  (it "returns the winner marker if there is one in the other diagonal"
    (should= u (winner-marker [u _ u
                               e u e
                               u e _]))))

(describe "is-winner-ai?"
  (it "returns false if winner has :is-ai? false"
    (should-not (is-winner-ai? [u e u
                                e u e
                                e e u]
                                human
                                easy-computer)))
  (it "returns true if winner is easy-computer"
    (should (is-winner-ai? [u u e
                            e e e
                            e e u]
                            human
                            easy-computer)))
  (it "returns true if winner is hard-computer"
    (should (is-winner-ai? [u u h
                            h h h
                            h h u]
                            human
                            hard-computer))))

(describe "winner-player"
  (it "returns human player if human won game"
    (should= human (winner-player [u u u
                                   e e _
                                   e e _]
                                   easy-computer
                                   human)))
  (it "should not return easy-computer player if human won game"
    (should-not (= easy-computer (winner-player [u u u
                                                 e e _
                                                 e e _]
                                                 easy-computer
                                                 human))))
  (it "returns easy-computer player if it won game"
    (should= easy-computer  (winner-player [e u u
                                            e _ u
                                            e u _]
                                            easy-computer
                                            human)))
  (it "returns hard-computer player if it won the game"
    (should= hard-computer (winner-player [h h h
                                           e e _
                                           _ _ _]
                                           hard-computer
                                           easy-computer))))

(describe "draw?"
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
                    e u e]))))

(describe "game-over?"
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
                         e u e]))))
