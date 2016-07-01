(ns ttt.core
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.game :as game]
            [ttt.helpers :as helpers]
            [ttt.player :as player]))


; TODO test
(defn -main
  []
  (println messenger/welcome)
  (println messenger/instructions)
  (println messenger/board-representation)
  (let [current-player (game/define-player
                            { :msg messenger/ask-first-player-marker })
              opponent (game/define-player
                            {:msg messenger/ask-second-player-marker
                             :opponent-marker (player/player-marker
                                               current-player) })]
    (game/play (board/new-board) current-player opponent)
    ))
