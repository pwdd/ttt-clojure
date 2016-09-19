(ns ttt.computer.medium
  (:require [ttt.get-spots :refer [select-spot]]
            [ttt.rules :as rules]
            [ttt.boards.board :as board]
            [ttt.helpers :as helpers]))


(defmethod select-spot :medium-computer
  [game-params]
  (rules/play-based-on-rules game-params))
