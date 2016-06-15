(ns ttt.game
  (require [ttt.board :as board]
           [ttt.user :as user]
           [ttt.messenger :as messenger]
           [ttt.computer :as computer]))

(def first-player { :type "human" :marker :x })
(def second-player { :type "computer" :marker :o })

(defn player-spot
  [player]
  (if (= "human" (player :type))
    (user/get-user-spot)
    (computer/computer-spot board/board-length)))

; TODO test
(defn valid-spot
  [board player]
  (let [spot (player-spot player)]
    (if (board/is-valid-move? board spot)
      spot
      (recur board player))))

; TODO test
(defn play
  [board current-player opponent]
  (let [spot (valid-spot board current-player)
        game-board (board/move board (current-player :marker) spot)]
    (println (messenger/moved-to current-player spot))
    (println (messenger/print-board game-board))
    (if (board/game-over? game-board)
      (println (messenger/result game-board))
      (recur game-board opponent current-player))))
