(ns ttt.boards.structure
  (:require [ttt.computer.negamax :as negamax]
            [ttt.boards.generator :as board-generator]
            [ttt.get-spots :refer [select-spot]]))

(defn board-unit
  [board]
  {:board board
   :best-move (select-spot {:board board
                            :current-player board-generator/current-player
                            :opponent board-generator/opponent
                            :depth negamax/start-depth})})

