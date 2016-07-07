(ns ttt.logic-spec
  (:require [speclj.core :refer :all]
            [ttt.logic :refer :all]
            [ttt.board :refer [draw? new-board]]
            [ttt.game :refer [create-game]]
            [ttt.player :refer [make-player]]))

(def first-player (make-player { :role :easy-computer :marker :x }))
(def second-player (make-player { :role :hard-computer :marker :o }))
(def game (create-game first-player second-player))
(def board (new-board))
