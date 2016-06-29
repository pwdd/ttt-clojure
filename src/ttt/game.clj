(ns ttt.game
  (:require [ttt.board :as board]
            [ttt.user :as user]
            [ttt.messenger :as messenger]
            [ttt.computer :as computer]
            [ttt.helpers :as helpers]
            [ttt.player :as player]))

(def acceptable-human-player
  #{ "h" "human" "hum" })

(def acceptable-easy-computer
  #{ "ec" "easy computer" "easycomputer" "easy"})

(def acceptable-hard-computer
  #{ "hc" "hard computer" "hardcomputer" "hard" "difficult"})

(defn valid-selection?
 [input]
 (or (contains? acceptable-human-player input)
     (contains? acceptable-easy-computer input)
     (contains? acceptable-hard-computer input)))

; TODO test
(defn get-marker
  [{ :keys [msg opponent-marker] :or { opponent-marker "" }}]
  (println msg)
  (let [marker (clojure.string/trim (read-line))]
    (if (helpers/valid-marker? marker opponent-marker)
      marker
      (recur { :msg msg :opponent-marker opponent-marker }))))

; TODO test
(defn get-role
  [marker]
  (messenger/ask-player-role marker)
  (let [input (helpers/clean-string (read-line))]
    (if (valid-selection? input)
      input
      (recur marker))))

(defn define-player
  [{ :keys [msg opponent-marker] :or { opponent-marker "" }}]
  (let [marker (get-marker { :msg msg :opponent-marker opponent-marker })
       role (get-role marker)]
    (cond
      (contains? acceptable-human-player role)
        (player/make-player { :marker marker
                              :role :human })
      (contains? acceptable-easy-computer role)
        (player/make-player { :marker marker
                              :role :easy-computer })
      :else
        (player/make-player { :marker marker
                              :role :hard-computer }))))

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
