(ns ttt.core
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.game :as game]))

; TODO test
(defn -main
  []
  (println messenger/welcome)
  (println messenger/instructions)
  (println messenger/board-representation)
  (let [current-player (game/define-player :x messenger/ask-first-player messenger/h-or-c)
        opponent (game/define-player :o messenger/ask-second-player messenger/h-or-c)]
    (game/play (board/new-board) current-player opponent)))
