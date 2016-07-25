(ns ttt.game-loop
  (:require [ttt.game :as game]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.board :as board]
            [ttt.negamax :as negamax]
            [ttt.get-spots :as spots]
            [ttt.player :as player]
            [ttt.rules :as rules]
            [ttt.prompt :as prompt]))

(def height 24)
(def final-msg-lines 9)
(def flush-down
  (clojure.string/join (repeat (/ (- height final-msg-lines) 2) "\n")))

(defn game-loop
  [game board current-player opponent]
  (if (and (board/is-board-empty? board)
           (= :human (player/role current-player)))
    (view/print-message (messenger/stringify-board board)))

    (let [spot (spots/select-spot current-player
                                  { :game game
                                    :board board
                                    :current-player current-player
                                    :opponent opponent
                                    :depth negamax/start-depth
                                    :board-length board/board-length })
          game-board (board/move board (player/marker current-player) spot)]

      (view/make-board-disappear (:role current-player))
      (view/print-message (messenger/moved-to game current-player spot))
      (view/print-message (messenger/stringify-board game-board))

      (if (rules/game-over? game-board)
        (view/print-message
          (messenger/result game game-board current-player opponent))
        (recur game game-board opponent current-player))))

(defn play
  []
  (view/clear-screen)
  (view/print-message messenger/welcome)
  (view/print-message messenger/instructions)
  (view/print-message messenger/board-representation)

  (let [current-player-attributes (prompt/get-player-attributes
                                    { :msg messenger/ask-first-marker-msg })
        opponent-attributes (prompt/get-player-attributes
                              { :msg messenger/ask-second-marker-msg
                                :opponent-marker (:marker current-player-attributes) })
        current-player (player/define-player current-player-attributes)
        opponent (player/define-player opponent-attributes)
        game (game/create-game (player/role current-player) (player/role opponent))]
    (game-loop game (board/new-board) current-player opponent))
    (println flush-down)
    (System/exit 0))
