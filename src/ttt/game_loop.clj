(ns ttt.game-loop
  (:require [ttt.game :as game]
            [ttt.messenger :as messenger]
            [ttt.board :as board]
            [ttt.negamax :as negamax]
            [ttt.get-spots :as spots]
            [ttt.player :as player]))
(defn play
  [game board current-player opponent]
  (if (and (board/is-board-empty? board) (= :human (player/role current-player)))
    (messenger/print-message (messenger/stringify-board board)))
  (let [spot (spots/select-spot current-player {  :game game
                                                  :board board
                                                  :current-player current-player
                                                  :opponent opponent
                                                  :depth negamax/start-depth
                                                  :board-length board/board-length })
        game-board (board/move board current-player spot)]
    (messenger/make-board-disappear current-player)
    (messenger/print-message (messenger/moved-to game current-player spot))
    (messenger/print-message (messenger/stringify-board game-board))
    (if (board/game-over? game-board)
      (messenger/print-message
        (messenger/result game game-board current-player opponent))
      (recur game game-board opponent current-player))))
