(ns ttt.game
  (require [ttt.board :as board]
           [ttt.user :as user]
           [ttt.messenger :as messenger]
           [ttt.computer :as computer]
           [ttt.helpers :as helpers]))

(defn valid-selection?
 [input]
 (or (= input "h")
     (= input "human")
     (= input "c")
     (= input "computer")))

(defn who-plays
 [prompt-msg]
 (let [input (helpers/clean-string (read-line))]
   (if (valid-selection? input)
     (str (nth input 0))
     (recur prompt-msg))))

; TODO test
(defn define-player
  [marker message prompt-msg]
  (println message)
  (let [type (who-plays prompt-msg)]
    (if (= type "h")
      {:type "human" :marker marker}
      {:type "computer" :marker marker})))

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

(defn game-type
  [first-player second-player]
  (if (not (= first-player second-player))
    :human-computer))

; TODO test
(defn play
  [board current-player opponent]
  (let [spot (valid-spot board current-player)
        game-board (board/move board (current-player :marker) spot)]
    (println (messenger/moved-to current-player spot))
    (println (messenger/print-board game-board))
    (if (board/game-over? game-board)
      (if (game-type (current-player :type) (opponent :type))
        (println (messenger/result-human-computer game-board current-player opponent))
        (println (messenger/result game-board)))
      (recur game-board opponent current-player))))
