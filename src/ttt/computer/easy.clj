(ns ttt.computer.easy
  (:require [ttt.board :as board]
            [ttt.helpers :as helpers]
            [ttt.get-spots :refer [select-spot]]))


(defmethod select-spot :easy-computer
  [player params]
  (helpers/random-move (board/available-spots (:board params))))
