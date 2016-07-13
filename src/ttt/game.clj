(ns ttt.game
  (:require [ttt.messenger :as messenger]
            [ttt.helpers :as helpers]))

(defrecord Game [type])

(defn game-type
  [first-player second-player]
  (let [first-name (messenger/stringify-role first-player)
        second-name (messenger/stringify-role second-player)]
    (helpers/write-game-type first-name second-name)))

(defn create-game
  [first-player second-player]
  (->Game (game-type first-player second-player)))
