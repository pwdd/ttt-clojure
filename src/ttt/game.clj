(ns ttt.game
  (:require [clojure.string :as string]
            [ttt.helpers :as helpers]
            [ttt.prompt :as prompt]
            [ttt.player :as player]
            [ttt.file-reader :as reader]
            [ttt.view :as view]
            [ttt.input-validation :as input-validation]))

(def file-extension ".json")

(defrecord Game [player-roles])

(defn same-roles?
  [first-player-role second-player-role]
  (= first-player-role second-player-role))

(defn computer-x-human?
  [first-player-role second-player-role]
  (and (some #{:human} [first-player-role second-player-role])
       (not (every? #{:human} [first-player-role second-player-role]))))

(defn game-players-roles
  [first-player-role second-player-role]
  (cond
    (same-roles? first-player-role second-player-role) :same-roles
    (computer-x-human? first-player-role second-player-role) :computer-x-human
    :else
      :computer-x-computer))

(defn create-game
  [first-player-role second-player-role]
  (->Game (game-players-roles first-player-role second-player-role)))

(defn setup-regular-game
  [msg-first-attr msg-second-attr]
  (let [current-player-attr (prompt/get-player-attributes { :msg msg-first-attr })
        opponent-attr (prompt/get-player-attributes { :msg msg-second-attr
                                                      :opponent-marker (:marker current-player-attr) })
        current-player (player/define-player current-player-attr)
        opponent (player/define-player opponent-attr)
        game (create-game (:role current-player) (:role opponent))]
    { :current-player current-player
      :opponent opponent
      :game game
      :saved false}))

(defn setup-resumed-game
  []
  (let [files (reader/names (reader/filenames (reader/files)))
        filename (prompt/choose-a-file files)
        data (reader/saved-data (str filename file-extension))
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
  (if (and (reader/is-there-any-file?)
           (= game-selection input-validation/saved-game-option))
    (setup-resumed-game)
    (setup-regular-game msg-first-player-attr msg-second-player-attr)))

(defn human-makes-first-move?
  [first-screen player-role]
  (and first-screen (= :human player-role)))
