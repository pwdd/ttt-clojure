(ns ttt.core
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.game :as game]
            [ttt.helpers :as helpers]
            [ttt.player :as player]
            [ttt.logic :as logic]))


; TODO test
(defn -main
  []
  (println messenger/welcome)
  (println messenger/instructions)
  (println messenger/board-representation)
  (let [current-player (game/define-player
                            { :msg messenger/ask-first-marker })
              opponent (game/define-player
                            {:msg messenger/ask-second-marker
                             :opponent-marker (player/marker
                                               current-player) })
        game (game/create-game current-player opponent)]
    (logic/play game (board/new-board) current-player opponent)
    ))
