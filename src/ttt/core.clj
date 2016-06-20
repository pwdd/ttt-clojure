(ns ttt.core
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.game :as game]))

(defn -main
  []
  (println messenger/instructions)
  (println messenger/board-representation)
  (game/play (board/new-board) game/first-player game/second-player)
  )
