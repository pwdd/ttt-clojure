(ns ttt.game
  (:require [ttt.helpers :as helpers]))

(defrecord Game [type player-roles])

(defn game-type
  [first-player second-player]
  (let [first-name (helpers/stringify-role first-player)
        second-name (helpers/stringify-role second-player)]
    (helpers/write-game-type first-name second-name)))

(defn game-players-roles
  [first-player-role second-player-role]
  (cond
    (= first-player-role second-player-role) :same-player-roles
    (and (some #{:human} [first-player-role second-player-role])
         (not (every? #{:human} [first-player-role second-player-role])))
      :computer-x-human
    :else
      :computer-x-computer))

(defn create-game
  [first-player-role second-player-role]
  (->Game (game-type first-player-role second-player-role)
          (game-players-roles first-player-role second-player-role)))
