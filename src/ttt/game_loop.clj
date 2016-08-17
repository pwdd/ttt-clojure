(ns ttt.game-loop
  (:require [ttt.game :as game]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.board :as board]
            [ttt.negamax :as negamax]
            [ttt.get-spots :as spots]
            [ttt.player :as player]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.prompt :as prompt]
            [ttt.file-reader :as reader]
            [ttt.input-validation :as input-validation]
            [ttt.file-reader :as file-reader]
            [ttt.easy-computer :as easy-computer]
            [ttt.animated-start :as animate]))

(def file-extension ".json")
(def first-player-color :green)
(def second-player-color :blue)
(def x {:token :x :color :green})
(def o {:token :o :color :blue})
(def start-animated-board [o o :_ :_ x :_ :_ :_ x])

(defn- setup-regular-game
  [msg-first-attr msg-second-attr]
  (let [current-player-attr (prompt/get-player-attributes {:msg msg-first-attr
                                                           :color first-player-color})
        opponent-attr
          (prompt/get-player-attributes {:msg msg-second-attr
                                         :color second-player-color
                                         :opponent-marker (:marker current-player-attr)})
        current-player (player/define-player current-player-attr :green)
        opponent (player/define-player opponent-attr :blue)
        game (game/create-game (:role current-player) (:role opponent))]
    {:current-player current-player
     :opponent opponent
     :game game
     :saved false}))

(defn- setup-resumed-game
  [directory]
  (let [files (reader/list-all-files directory)
        filename (prompt/choose-a-file files)
        data (reader/saved-data (str filename file-extension) directory)
        current-player (data :current-player-data)
        opponent (data :opponent-data)
        game (game/create-game (:role current-player)
                               (:role opponent))
        board (data :board-data)]
    {:current-player current-player
     :opponent opponent
     :game game
     :board board
     :saved true}))

(defn game-setup
  [game-selection directory & [msg-first-player-attr msg-second-player-attr]]
  (if (= game-selection input-validation/saved-game-option)
    (setup-resumed-game directory)
    (setup-regular-game msg-first-player-attr msg-second-player-attr)))

(defn first-view-msgs
  []
  (view/clear-screen)
  (animate/animated-board :computer-x-computer
                  start-animated-board
                  {:marker x :role :hard-computer}
                  {:marker o :role :easy-computer})
  (view/print-message messenger/welcome))

(defn initial-view-of-board
  [first-screen saved player board]
  (when (game/human-makes-first-move? first-screen (:role player))
    (if saved
      (view/print-message (messenger/current-player-is (:marker player))))
    (view/print-message (messenger/stringify-board board))))

(defn display-new-board-info
  [game board current-player spot]
  (view/make-board-disappear (:role current-player) 1000)
  (view/print-message (messenger/moved-to game current-player spot))
  (view/print-message (messenger/stringify-board board)))

(defn make-a-move
  [board current-player opponent]
  (spots/select-spot current-player
                    {:board board
                     :current-player current-player
                     :opponent opponent
                     :depth negamax/start-depth
                     :board-length board/board-length}))

(defn game-over-msg
  [game board current-player opponent]
  (view/print-message (messenger/result game
                                        board
                                        current-player
                                        opponent)))

(defn game-loop
  [{:keys [game board current-player opponent saved first-screen]
    :or {board (board/new-board 3) first-screen true}}]

  (initial-view-of-board first-screen saved current-player board)

  (let [spot (make-a-move board current-player opponent)
        game-board (board/move board spot (:marker current-player))]

    (display-new-board-info game game-board current-player spot)

    (if (evaluate-game/game-over? game-board)
      (game-over-msg game game-board current-player opponent)
      (recur {:game game
              :board game-board
              :opponent current-player
              :current-player opponent
              :saved saved
              :first-screen false}))))

(defn game-selection
  [directory]
  (if (reader/is-there-any-file? directory)
    (prompt/get-new-or-saved)
    input-validation/new-game-option))

(defn setup
  []
  (let [selection (game-selection file-reader/directory)]
    (game-setup selection
                file-reader/directory
                messenger/ask-first-marker-msg
                messenger/ask-second-marker-msg)))

(defn clean-and-exit
  []
  (println view/flush-down)
  (System/exit 0))

(defn play
  []
  (first-view-msgs)
  (game-loop (setup))
  (clean-and-exit))
