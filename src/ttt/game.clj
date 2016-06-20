(ns ttt.game
  (require [ttt.board :as board]
           [ttt.user :as user]
           [ttt.messenger :as messenger]))

(def first-player :x)
(def second-player :o)



; TODO test
(defn play
  [board current-player opponent]
  (let [spot (user/get-valid-user-input board)
        game-board (board/move board current-player spot)]
    (println (messenger/print-board game-board))
    (if (board/game-over? game-board current-player opponent)
      (println (messenger/result game-board current-player opponent))
      (recur game-board opponent current-player))))
