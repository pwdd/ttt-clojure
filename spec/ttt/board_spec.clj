(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]
            [ttt.player :as player]))

(describe "board-size"
  (it "has a default size"
    (should= 3 board-size)))

(describe "board-length"
  (it "has the default value of squared board-size"
    (should= (* board-size board-size) board-length)))

(describe "winning-combos"
  (it "holds all winning combination"
    (should= 8 (count winning-combos))))

(describe "new-board"
  (it "is a vector of empty spots"
    (should (every? #{empty-spot} (new-board))))
  (it "has length equal to board-length"
    (should (= board-length (count (new-board))))))

(describe "move"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        _ empty-spot
        empty-board (new-board)]
    (it "sets a value to an empty board"
      (should= [u _ _ _ _ _ _ _ _]
               (move empty-board u 0)))
    (it "sets a spot on a board with some spots already taken"
      (should= [u _ _ u _ u _ _ _]
               (move [u _ _ u _ _ _ _ _] u 5)))))

(describe "is-spot-available?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        _ empty-spot
        empty-board (new-board)]
    (it "returns true when spot is not taken"
        (should (is-spot-available? empty-board 0)))
     (it "returns false when spot is taken"
        (should-not (is-spot-available? [u _ _ _ _ _ _ _ _] 0)))))

(describe "is-board-full?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        _ empty-spot
        empty-board (new-board)]
    (it "returns true if board is full"
      (should (is-board-full? (vec (repeat board-length u)))))
    (it "returns false if board is empty"
      (should-not (is-board-full? empty-board)))
    (it "returns false if there is any spot available"
      (should-not (is-board-full? [u u u u u u u u _])))))

(describe "is-board-empty?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        _ empty-spot
        empty-board (new-board)]
    (it "returns true if board has only empty spots"
      (should (is-board-empty? empty-board)))
    (it "returns false if any spot is taken"
      (should-not (is-board-empty? [_ _ _ _ _ u _ _ _])))))

(describe "available-spots"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        _ empty-spot
        full-board (vec (repeat board-length u))]
    (it "returns a collection containing the index of the only available spot"
      (should= '(8) (available-spots [u u u u u u u u _])))
    (it "returns a collection containing the indexes of all available spots"
      (should= '(0 1 8) (available-spots [_ _ u u u u u u _])))
    (it "returns an empty collection if no spot is available"
      (should= '() (available-spots full-board)))))

(describe "is-valid-move?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        _ empty-spot
        empty-board (new-board)]
    (it "returns true if number is the index of an available spot on board"
      (should (is-valid-move? empty-board 0)))
    (it "returns true if number is the index of one of the available spots"
      (should (is-valid-move? [u _ u _ _ _ _ _ _] 3)))
    (it "returns false if spot is taken"
      (should-not (is-valid-move? [_ u u _ _ _ _ _ _] 1)))
    (it "returns false if number is bigger than board-length"
      (should-not (is-valid-move? empty-board 10)))
    (it "returns false if number is negative"
      (should-not (is-valid-move? empty-board -10)))))

(describe "repeated?"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        easy-computer (player/make-player { :role :easy-computer :marker :e })
        e (player/marker easy-computer)
        _ empty-spot
        empty-board (new-board)]
    (it "returns false if board is empty"
      (should-not (repeated? empty-board [0 1 2])))
    (it "returns false if there is no repetition on given indexes"
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
                          u e _] [2 4 6])))
    (it "returns true if there are repeated elements in a column"
      (should (repeated? [e _ u
                          e u _
                          e u _] [0 3 6])))))

(describe "find-repetition"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        easy-computer (player/make-player { :role :easy-computer :marker :e })
        e (player/marker easy-computer)
        _ empty-spot
        empty-board (new-board)]
    (it "returns an empty collection if board is empty"
      (should= '()
               (find-repetition empty-board)))
    (it "returns an empty collection if board is full and there is no winner"
      (should= '()
               (find-repetition [u e u
                                 e u e
                                 e u e])))
    (it "identifies a single combo with indexes of repeated markers"
      (should= '([0 1 2])
               (find-repetition [u u u
                                 e _ _
                                 e e _])))
    (it "identifies multiple combos with indexes of repeated markers"
      (should= '([3 4 5] [6 7 8])
                (find-repetition [u e _
                                  e e e
                                  u u u])))))

(describe "winning-combo"
  (let [human (player/make-player { :marker :x :role :human })
        u (player/marker human)
        easy-computer (player/make-player { :role :easy-computer :marker :e })
        e (player/marker easy-computer)
        _ empty-spot
        empty-board (new-board)]
    (it "returns nil when board is empty"
      (should-not (winning-combo empty-board)))
    (it "returns nil if finds a sequence of 3 empty spots and there is no winner"
      (should-not (winning-combo [u e u
                                  _ _ _
                                  u _ e])))
    (it "returns winning combo instead of the 3 repeated empty spots in the board"
      (should= [3 4 5] (winning-combo [_ _ _
                                       u u u
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
                                       e u u])))))
