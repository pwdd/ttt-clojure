(ns ttt.get-spots-spec
  (:require [speclj.core :refer :all]
            [ttt.get-spots :refer :all]
            [ttt.player :refer [make-player]]
            [ttt.board :refer [new-board available-spots]]))

(def human (make-player { :role :human :marker :x }))
(def easy-computer (make-player { :role :easy-computer :marker :o }))

(describe "select-spot"
  (context ":human"
    (it "returns an integer"
      (should= 0 (with-in-str "1"
                 (select-spot human { :board (new-board) }))))
    (it "returns an integer that numeric string minus one"
      (should= 3 (with-in-str "4"
                 (select-spot human { :board (new-board) })))))
  (context ":easy-computer"
    (with spots (available-spots [:x :_ :o :_ :o :_ :_ :x :o :o]))
    (it "returns a random index from the available-spots"
      (should (some #{ (select-spot easy-computer
                                    { :board [:x :_ :o
                                              :_ :o :_
                                              :_ :x :o] })}
                    @spots)))))
