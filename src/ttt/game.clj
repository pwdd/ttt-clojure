(ns ttt.game
  (:require [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.helpers :as helpers]
            [ttt.player :as player]
            [ttt.get-spots :as spots]
            [ttt.input-validation :as input-validation]))

(defrecord Game [type])

(defn game-type
  [first-player second-player]
  (let [first-name (messenger/stringify-role first-player)
        second-name (messenger/stringify-role second-player)]
    (messenger/write-game-type first-name second-name)))

(defn create-game
  [first-player second-player]
  (->Game (game-type first-player second-player)))

; TODO test // only base case is tested
(defn get-marker
  [{ :keys [msg opponent-marker] :or { opponent-marker "" } }]
  (messenger/print-message msg)
  (let [marker (messenger/ask-player-marker)]
    (if (input-validation/is-valid-marker? marker opponent-marker)
      marker
      (do
        (messenger/print-message
          (messenger/invalid-marker-msg marker opponent-marker))
        (recur { :msg msg
                 :opponent-marker opponent-marker })))))

; TODO test
(defn get-role
  [marker]
  (messenger/print-message (messenger/ask-role marker))
  (let [input (messenger/ask-player-role)]
    (if (input-validation/is-valid-role-selection? input)
      input
      (do
        (messenger/print-message messenger/invalid-role-msg)
        (recur marker)))))

(defn define-player
  [{ :keys [msg opponent-marker] :or { opponent-marker "" }}]
  (let [marker (get-marker { :msg msg :opponent-marker opponent-marker })
       role (get-role marker)]
    (cond
      (input-validation/is-acceptable-as-human-player? role)
        (player/make-player { :marker marker
                              :role :human })
      (input-validation/is-acceptable-as-easy-computer? role)
        (player/make-player { :marker marker
                              :role :easy-computer })
      :else
        (player/make-player { :marker marker
                              :role :hard-computer }))))
