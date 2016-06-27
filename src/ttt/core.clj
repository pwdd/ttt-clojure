(ns ttt.core
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.game :as game]
            [ttt.helpers :as helpers]
            [ttt.player :as player]
            [ttt.game-loop :as game-loop]))


; TODO test
(defn -main
  []
  (messenger/print-message messenger/welcome)
  (messenger/print-message messenger/instructions)
  (messenger/print-message (messenger/build-board-representation))
  (let [current-player (game/define-player
                            { :msg messenger/ask-first-marker })
              opponent (game/define-player
                            {:msg messenger/ask-second-marker
                             :opponent-marker (player/marker
                                               current-player) })
        game (game/create-game current-player opponent)]
    (game-loop/play game (board/new-board) current-player opponent)
    ))
