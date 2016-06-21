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
  (let [current-player (game/define-player :x messenger/ask-first-player)
        opponent (game/define-player :o messenger/ask-second-player)]
    (game/play (board/new-board) current-player opponent)))
