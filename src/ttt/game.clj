(ns ttt.game
  (:require [ttt.helpers :as helpers]
            [ttt.prompt :as prompt]
            [ttt.player :as player]
            [ttt.file-reader :as reader]
            [ttt.view :as view]))

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

(defn setup-regular-game
  [msg-first-player-attr msg-second-player-attr]
  (let [current-player-attributes
         (prompt/get-player-attributes { :msg msg-first-player-attr })
        opponent-attributes
          (prompt/get-player-attributes
              { :msg msg-second-player-attr
                :opponent-marker (:marker current-player-attributes) })
        current-player (player/define-player current-player-attributes)
        opponent (player/define-player opponent-attributes)
        game (create-game (:role current-player) (:role opponent))]
    { :current-player current-player
      :opponent opponent
      :game game
      :saved false}))

(defn setup-resumed-game
  []
  (let [files (reader/names (reader/filenames (reader/files)))
        filename (prompt/choose-a-file files)
        data (reader/saved-data (str filename ".json"))
        current-player-attributes (data :current-player-data)
        opponent-attributes (data :opponent-data)
        current-player (player/define-player current-player-attributes)
        opponent (player/define-player opponent-attributes)
        game (create-game (:role current-player)
                               (:role opponent))
        board (data :board-data)]
    { :current-player current-player
      :opponent opponent
      :game game
      :board board
      :saved true}))


(defn game-setup
  [game-selection & [msg-first-player-attr msg-second-player-attr]]
  (if (reader/is-there-any-file?)
    (if (= game-selection "1")
      (setup-resumed-game)
      (setup-regular-game msg-first-player-attr msg-second-player-attr))
    (setup-regular-game msg-first-player-attr msg-second-player-attr)))

(defn human-makes-first-move?
  [first-screen player-role]
  (and first-screen (= :human player-role)))
