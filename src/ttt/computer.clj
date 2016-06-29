(ns ttt.computer
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(defn computer-spot
  [board-length]
  (rand-int board-length))

(defn negamax [board current-player opponent]
  (if (board/game-over? board)
    (* (player/player-value current-player)
       (board/winner-value board current-player opponent))
    "else"))

; (comment
;   negamax (board player-value alpha beta depth)
;
;   if game is over or depth >= max-depth
;     (player-value * board-analyses)
;   else
;     max-value = -infinity
;     for each spot in available-spots
;       new-board = move player to spot in board
;       score = - negamax(new-board -player-value -beta -alpha depth+1)
;       if score > max-value
;         max-value = score
;         best-move = spot
;       if score > alpha
;         alpha = score
;       if alpha >= beta
;         alpha
;         best-move = spot
;     endfor
;   best-move
; )
