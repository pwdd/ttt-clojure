(ns ttt.easy-computer
  (:require [ttt.board :as board]
            [ttt.get-spots :refer [select-spot]]))


(defmethod select-spot :easy-computer
  [player params]
  (rand-nth (board/available-spots (:board params))))
