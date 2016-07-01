(ns ttt.board-spec
  (:require [speclj.core :refer :all]
            [ttt.board :refer :all]
            [ttt.player :refer [make-player]]))

(def human (make-player { :marker :x :role :human }))
(def computer (make-player { :role :easy-computer :marker :o }))
(def hard-computer (make-player { :role :hard-computer :marker :o }))

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
    (should= [:x :_ :_ :_ :_ :_ :_ :_ :_]
             (move [:_ :_ :_ :_ :_ :_ :_ :_ :_] human 0)))
  (it "sets a spot on a board with some spots already taken"
    (should= [:x :_ :_ :o :_ :x :_ :_ :_]
             (move [:x :_ :_ :o :_ :_ :_ :_ :_] human 5))))

(describe "is-available?"
  (it "returns true when spot is not taken"
      (should (is-available? [:_ :_ :_ :_ :_ :_ :_ :_ :_] 0)))
   (it "returns false when spot is taken"
      (should-not (is-available? [:x :_ :_ :_ :_ :_ :_ :_ :_] 0))))

(describe "is-full?"
  (it "returns true if board is full"
    (should (is-full? [:x :x :x :x :x :x :x :x :x])))
  (it "returns false if board is empty"
    (should-not (is-full? [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns false if there is any spot available"
    (should-not (is-full? [:x :x :x :x :x :x :x :x :_]))))

(describe "is-empty?"
  (it "returns true if board has only empty spots"
    (should (is-empty? [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns false if any spot is taken"
    (should-not (is-empty? [:_ :_ :_ :_ :_ :x :_ :_ :_]))))

(describe "available-spots"
  (it "returns a list with one element if only one spot is available"
    (should= '(8) (available-spots [:x :x :x :x :x :x :x :x :_])))
  (it "returns a vector with more than one spot available"
    (should= '(0 1 8) (available-spots [:_ :_ :x :x :x :x :x :x :_])))
  (it "returns an empty vector is no spot is available"
    (should= '() (available-spots [[:x :x :x :x :x :x :x :x :x]]))))

(describe "is-valid-move?"
  (it "returns true if input is valid move"
    (should (is-valid-move? [:_ :_ :_ :_ :_ :_ :_ :_ :_] 0)))
  (it "returns true if is valid move on a board with spots taken"
    (should (is-valid-move? [:x :_ :o :_ :_ :_ :_ :_ :_] 3)))
  (it "returns false if spot is taken"
    (should-not (is-valid-move? [:_ :x :o :_ :_ :_ :_ :_ :_] 1)))
  (it "returns false if input is out of range"
    (should-not (is-valid-move? [:_ :x :o :_ :_ :_ :_ :_ :_] 10))))

(describe "repeated?"
  (it "returns false if board is empty"
    (should-not (repeated? [:_ :_ :_ :_ :_ :_ :_ :_ :_] [0 1 2])))
  (it "returns false if there is no repetition on board postions"
    (should-not (repeated? [:x :o :x
                          :o :x :o
                          :o :x :x] [0 1 2])))
  (it "returns true if there are repeated elements in a row"
    (should (repeated? [:x :x :x
                      :o :_ :_
                      :o :o :_] [0 1 2])))
  (it "returns true if there are repeated elements in a diagonal"
    (should (repeated? [:o :_ :x
                      :o :x :_
                      :x :o :_] [2 4 6]))))

(describe "find-repetition"
  (it "returns an empty list if board is empty"
    (should= '()
             (find-repetition [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns an empty list if board is full and there is no winner"
    (should= '()
             (find-repetition [:x :o :x
                               :o :x :o
                               :o :x :o])))
  (it "identifies a single combo with repeated markers"
    (should= '([0 1 2])
             (find-repetition [:x :x :x
                               :o :_ :_
                               :o :o :_])))
  (it "identifies multiple combos with repeated markers"
    (should= '([3 4 5] [6 7 8])
              (find-repetition [:x :o :_
                                :o :o :o
                                :x :x :x]))))

(describe "winning-combo"
  (it "returns nothing when board is empty"
    (should-not (winning-combo [:_ :_ :_
                                :_ :_ :_
                                :_ :_ :_])))
  (it "returns nothing if finds 3 empty spots and there is no winner"
    (should-not (winning-combo [:x :o :x
                                :_ :_ :_
                                :x :_ :o])))
  (it "returns winning row even if there are 3 empty spots in the board"
    (should= [0 1 2] (winning-combo [:x :x :x
                                     :_ :_ :_
                                     :x :_ :o])))
  (it "returns winning row"
    (should= [0 1 2] (winning-combo [:x :x :x
                                     :o :o :_
                                     :o :_ :_])))
  (it "returns winning column"
    (should= [1 4 7] (winning-combo [:x :o :x
                                     :_ :o :_
                                     :x :o :x])))
  (it "returns winning diagonal"
    (should= [2 4 6] (winning-combo [:x :_ :o
                                     :_ :o :x
                                     :o :x :x]))))

(describe "winner-mark"
  (it "returns nil if board is empty"
    (should (nil? (winner-mark [:_ :_ :_ :_ :_ :_ :_ :_ :_]))))
  (it "returns nil if there is no winner"
    (should (nil? (winner-mark [:x :o :x
                           :o :x :o
                           :o :x :o]))))
  (it "returns the winner if there is one on rows"
    (should= :x (winner-mark [:x :x :x
                         :_ :_ :o
                         :o :o :_])))
  (it "returns the winner if there is one in second row"
    (should= :x (winner-mark [:_ :_ :o
                         :x :x :x
                         :o :o :_])))
  (it "returns the winner if there is one in column"
    (should= :x (winner-mark [:x :_ :_
                         :x :o :o
                         :x :_ :_])))
  (it "returns the winner if there is one in the second column"
    (should= :x (winner-mark [:_ :x :_
                         :o :x :o
                         :o :x :_])))
  (it "returns the winner if there is one in a diagonal"
    (should= :o (winner-mark [:o :x :x
                         :x :o :_
                         :_ :_ :o])))
  (it "returns the winner if there is one in the other diagonal"
    (should= :x (winner-mark [:x :_ :x
                         :o :x :o
                         :x :o :_]))))

(describe "is-winner-ai?"
  (it "returns false if winner has :is-ai? false"
    (should-not (is-winner-ai? [:x :o :x
                                :o :x :o
                                :o :o :x]
                                human
                                computer)))
  (it "returns true if winner has :is-ai? true"
    (should (is-winner-ai? [:x :x :o
                            :o :o :o
                            :o :o :x]
                             human
                             computer))))

(describe "draw?"
  (it "returns false if board is empty"
    (should-not (draw? [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns false if board is not full"
    (should-not (draw? [:x :x :x :x :x :x :x :x :_])))
  (it "returns false if board is full and there is a winner"
    (should-not (draw? [:x :o :x
                        :o :x :o
                        :o :o :x])))
  (it "returns true if board is full and there is no winner"
    (should (draw? [:x :o :x
                    :o :x :o
                    :o :x :o]))))

(describe "game-over?"
  (it "returns false if board is empty"
    (should-not (game-over? [:_ :_ :_ :_ :_ :_ :_ :_ :_])))
  (it "returns false if only some spots are taken"
    (should-not (game-over? [:x :o :_ :_ :_ :x :_ :_ :o])))
  (it "returns true if there is a draw"
    (should (game-over? [:x :o :x
                         :o :x :o
                         :o :x :o])))
  (it "returns true if there is a winner"
    (should (game-over? [:x :x :x
                         :o :_ :o
                         :o :x :o]))))
