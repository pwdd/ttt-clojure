(ns ttt.game
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.helpers :as helpers]
            [ttt.player :as player]
            [ttt.spots :as spots]))

(defrecord Game [type])

(defn game-type
  [first-player second-player]
  (cond
    (= :human (player/role first-player) (player/role second-player))
      :human-x-human

    (= :hard-computer (player/role first-player) (player/role second-player))
      :hard-x-hard

    (= :easy-computer (player/role first-player) (player/role second-player))
      :easy-x-easy

    (and (= :human (player/role first-player))
         (= :hard-computer (player/role second-player))) :human-x-hard

    (and (= :hard-computer (player/role first-player))
         (= :human (player/role second-player))) :human-x-hard

    (and (= :human (player/role first-player))
         (= :easy-computer (player/role second-player))) :human-x-easy

    (and (= :easy-computer (player/role first-player))
         (= :human (player/role second-player))) :human-x-easy

    (and (= :easy-computer (player/role first-player))
         (= :hard-computer (player/role second-player))) :easy-x-hard

    (and (= :hard-computer (player/role first-player))
         (= :easy-computer (player/role second-player))) :easy-x-hard
    :else
      :not-game-type))

(defn create-game
  [first-player second-player]
  (->Game (game-type first-player second-player)))

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
  [{ :keys [msg opponent-marker] :or { opponent-marker "" } }]
  (messenger/print-message msg)
  (let [marker (messenger/ask-player-marker)]
    (if (valid-marker? marker opponent-marker)
      marker
      (do
        (messenger/print-message
          (messenger/invalid-marker-msg marker opponent-marker))
        (recur { :msg msg
                 :opponent-marker opponent-marker })))))

; TODO test
(defn get-role
  [marker]
  (messenger/ask-role marker)
  (let [input (helpers/clean-string (read-line))]
    (if (valid-selection? input)
      input
      (do
        (messenger/print-message messenger/invalid-role-msg)
        (recur marker)))))

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
