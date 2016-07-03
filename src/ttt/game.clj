(ns ttt.game
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.helpers :as helpers]
            [ttt.player :as player]
            [ttt.spots :as spots]
            [ttt.negamax :as negamax]))

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

(defn valid-marker?
 [input opponent-marker]
 (and (= (count input) 1)
      (re-matches #"^[a-zA-Z]$" input)
      (not (= input opponent-marker))))

; TODO test
(defn get-marker
  [{ :keys [msg opponent-marker] :or { opponent-marker "" }}]
  (println msg)
  (let [marker (clojure.string/trim (read-line))]
    (if (valid-marker? marker opponent-marker)
      marker
      (recur { :msg msg :opponent-marker opponent-marker }))))

; TODO test
(defn get-role
  [marker]
  (messenger/ask-role marker)
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

; TODO test
(defn validate-spot
  [player params]
  (let [spot (spots/select-spot player params)]
    (if (board/is-valid-move? (:board params) spot)
      spot
      (recur player params))))

; TODO test
(defn play
  [board current-player opponent]
  (let [spot (validate-spot current-player { :board board
                                             :current-player current-player
                                             :opponent opponent
                                             :depth negamax/start-depth
                                             :board-length board/board-length})
        game-board (board/move board current-player spot)]
    (println (messenger/moved-to current-player spot))
    (println (messenger/print-board game-board))
    (if (board/game-over? game-board)
      (if (negamax/game-type current-player opponent)
        (println (messenger/result-human-computer
                  game-board current-player opponent))
        (println (messenger/result game-board)))
      (recur game-board opponent current-player))))
