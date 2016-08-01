(ns ttt.game-loop
  (:require [clojure.string :as string]
            [ttt.game :as game]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.board :as board]
            [ttt.negamax :as negamax]
            [ttt.get-spots :as spots]
            [ttt.player :as player]
            [ttt.rules :as rules]
            [ttt.prompt :as prompt]
            [ttt.file-reader :as reader]))

(defn game-loop
  [{ :keys [game board current-player opponent saved first-screen filename]
     :or { board (board/new-board)
           first-screen true }}]

  (if (game/human-makes-first-move? first-screen (player/role current-player))
    (do
      (if saved
        (view/print-message
          (messenger/current-player-is
            (player/marker current-player))))
      (view/print-message (messenger/stringify-board board))))

  (let [spot (spots/select-spot current-player
                                { :board board
                                  :current-player (player/marker current-player)
                                  :opponent (player/marker opponent)
                                  :depth negamax/start-depth
                                  :board-length board/board-length })
        game-board (board/move board spot (player/marker current-player))]

    (view/make-board-disappear (player/role current-player))
    (view/print-message (messenger/moved-to game current-player spot))
    (view/print-message (messenger/stringify-board game-board))

    (if (rules/game-over? game-board)
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

; This is hard to avoid, but the `game-loop` function is one I would have nightmares about having to
; change. You might be able to make things a bit easier to follow if you can extract various steps
; of the game loop into smaller, well-named functions. As you do that, you may find that you can
; think of a better home for the extracted sub-function, and then move that function to become
; part of the public interface of one of your other modules.

; In general, it might benefit you to take a step back and think about what the simplest, most
; abstract game loop might look like. Perhaps something like:

; fn play ->
;   set-up-game
;   loop game-loop
;   wrap-up-game

; fn game-loop ->
;   show-game-state
;   get-move
;   make-move

; and think about how you can start extracting things to get to that level of simplicity. The more
; that you're able to pull out of here and instead consider to be part of the functionality of
; distinct, independent modules of your app - the simpler this code will get and the easier
; everything will be to test.
