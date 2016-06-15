(ns ttt.game
  (require [ttt.board :as board]
           [ttt.user :as player]
           [ttt.messenger :as messenger]))

(def first-player :x)
(def second-player :o)

(defn play
  [board current-player opponent]
  (let [input (player/get-valid-input board)
        game-board (board/move board current-player input)]
    (println (messenger/print-board game-board))
    (if (board/game-over? game-board)
      (println (messenger/result game-board))
      (recur game-board opponent current-player)
    )
  )
)
