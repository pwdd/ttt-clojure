(ns ttt.computer.easy
  (:require [ttt.board :as board]
            [ttt.helpers :as helpers]
            [ttt.get-spots :refer [select-spot]]))


(defmethod select-spot :easy-computer
  [game-params]
  (helpers/random-move (board/available-spots (:board game-params))))
