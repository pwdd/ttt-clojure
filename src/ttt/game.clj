(ns ttt.game
  (require [ttt.board :as b]
           [ttt.user :as player]
           [ttt.messenger :as messenger]))

(defn play
  [board current-player opponent]
  (let [input (player/get-valid-input board)
        game-board (b/move board current-player input)]
    (println (messenger/print-board game-board))
    (if (b/game-over? game-board)
      (println (messenger/result game-board))
      (recur game-board opponent current-player)
    )
  )
)
