(ns ttt.get-spots-spec
  (:require [speclj.core :refer :all]
            [ttt.get-spots :refer :all]
            [ttt.player :as player]
            [ttt.board :as board]
            [ttt.prompt :as prompt]))

(describe "select-spot"
  (around [it]
    (with-redefs [prompt/prompt (fn [_] _)])
    (with human (player/make-player { :role :human :marker :x }))
    (with easy-computer (player/make-player { :role :easy-computer :marker :o })))
  (context ":human"
    (it "returns an integer"
      (should= 0 (with-in-str "1"
                 (select-spot @human { :board (board/new-board) }))))
    (it "returns an integer that numeric string minus one"
      (should= 3 (with-in-str "4"
                 (select-spot @human { :board (board/new-board) })))))
  (context ":easy-computer"
    (with spots (board/available-spots [:x :_ :o :_ :o :_ :_ :x :o :o]))
    (it "returns a random index from the available-spots"
      (should (some #{ (select-spot @easy-computer
                                    { :board [:x :_ :o
                                              :_ :o :_
                                              :_ :x :o] })}
                    @spots)))))
