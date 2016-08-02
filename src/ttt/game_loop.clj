(ns ttt.game-loop
  (:require [clojure.string :as string]
            [ttt.game :as game]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.board :as board]
            [ttt.negamax :as negamax]
            [ttt.get-spots :as spots]
            [ttt.player :as player]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.prompt :as prompt]
            [ttt.file-reader :as reader]))

(defn game-loop
  [{ :keys [game board current-player opponent saved first-screen filename]
     :or { board (board/new-board)
           first-screen true }}]

  (if (game/human-makes-first-move? first-screen (:role current-player))
    (do
      (if saved
        (view/print-message
          (messenger/current-player-is
            (:marker current-player))))
      (view/print-message (messenger/stringify-board board))))

  (let [spot (spots/select-spot current-player
                                { :board board
                                  :current-player (:marker current-player)
                                  :opponent (:marker opponent)
                                  :depth negamax/start-depth
                                  :board-length board/board-length })
        game-board (board/move board spot (:marker current-player))]

    (view/make-board-disappear (:role current-player))
    (view/print-message (messenger/moved-to game current-player spot))
    (view/print-message (messenger/stringify-board game-board))

    (if (evaluate-game/game-over? game-board)
      (view/print-message
        (messenger/result game game-board current-player opponent))
      (recur {:game game
              :board game-board
              :opponent current-player
              :current-player opponent
              :saved saved
              :first-screen false }))))

(defn play
  []
  (view/clear-screen)
  (view/print-message messenger/welcome)
  (view/print-message messenger/instructions)
  (view/print-message messenger/board-representation)

  (let [game-selection (prompt/get-new-or-saved)
        setup (game/game-setup game-selection
                               messenger/ask-first-marker-msg
                               messenger/ask-second-marker-msg)]
    (game-loop setup))

  (println view/flush-down)
  (System/exit 0))
