(ns ttt.game
  (:require [ttt.board :as board]
            [ttt.user :as user]
            [ttt.messenger :as messenger]
            [ttt.computer :as computer]
            [ttt.helpers :as helpers]
            [ttt.player :as player])
  ;(:import [ttt.player Player])
  )

(def acceptable-human-player #{ "h" "human" })
(def acceptable-computer-player #{ "c" "computer" })

(defn valid-selection?
 [input]
 (or (contains? acceptable-human-player input)
     (contains? acceptable-computer-player input)))

; TODO test
(defn who-plays
 []
 (println messenger/h-or-c)
 (let [input (helpers/clean-string (read-line))]
   (if (valid-selection? input)
     input
     (recur))))

(defn define-player
  [marker & [msg]]
  (if msg
    (println msg))
  (let [role (who-plays)]
    (if (contains? acceptable-human-player role)
      (player/make-player {:marker marker :ai false})
      (player/make-player {:marker marker :ai true}))))

(defn player-spot
  [player]
  (if (player/is-ai? player)
    (computer/computer-spot board/board-length)
    (user/get-user-spot)))

; TODO test
(defn valid-spot
  [board player]
  (let [spot (player-spot player)]
    (if (board/is-valid-move? board spot)
      spot
      (recur board player))))

(defn game-type
  [first-player second-player]
  (if (not (= (player/is-ai? first-player)
              (player/is-ai? second-player)))
    :human-computer))

; TODO test
(defn play
  [board current-player opponent]
  (let [      spot (valid-spot board current-player)
        game-board (board/move board current-player spot)]
    (println (messenger/moved-to current-player spot))
    (println (messenger/print-board game-board))
    (if (board/game-over? game-board)
      (if (game-type current-player opponent)
        (println (messenger/result-human-computer
                  game-board current-player opponent))
        (println (messenger/result game-board)))
      (recur game-board opponent current-player))))
