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
            [ttt.file-reader :as reader]))

(defn first-view-msgs
  []
  (view/clear-screen)
  (view/print-message messenger/welcome)
  (view/print-message messenger/instructions)
  (view/print-message messenger/board-representation))

(defn initial-view-of-board
  [first-screen saved player board]
  (when (game/human-makes-first-move? first-screen (:role player))
    (if saved
      (view/print-message (messenger/current-player-is (:marker player))))
    (view/print-message (messenger/stringify-board board))))

(defn display-new-board-info
  [game board current-player spot]
  (view/make-board-disappear (:role current-player))
  (view/print-message (messenger/moved-to game current-player spot))
  (view/print-message (messenger/stringify-board board)))

(defn make-a-move
  [board current-player opponent]
  (spots/select-spot current-player
                   { :board board
                     :current-player (:marker current-player)
                     :opponent (:marker opponent)
                     :depth negamax/start-depth
                     :board-length board/board-length }))

(defn game-over-msg
  [game board current-player opponent]
  (view/print-message (messenger/result game
                                        board
                                        current-player
                                        opponent)))

(defn game-loop
  [{ :keys [game board current-player opponent saved first-screen]
     :or { board (board/new-board)
           first-screen true }}]

  (initial-view-of-board first-screen saved current-player board)

  (let [spot (make-a-move board current-player opponent)
        game-board (board/move board spot (:marker current-player))]

    (display-new-board-info game game-board current-player spot)

    (if (evaluate-game/game-over? game-board)
      (game-over-msg game game-board current-player opponent)
      (recur { :game game
               :board game-board
               :opponent current-player
               :current-player opponent
               :saved saved
               :first-screen false }))))

(defn setup
  []
  (let [game-selection (prompt/get-new-or-saved)]
    (game/game-setup game-selection
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
