(ns ttt.computer.medium
  (:require [ttt.get-spots :refer [select-spot]]
            [ttt.rules :as rules]
            [ttt.board :as board]
            [ttt.helpers :as helpers]))


(defmethod select-spot :medium-computer
  [player params]
  (rules/play-based-on-rules player params))
