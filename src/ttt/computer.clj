(ns ttt.computer
  (:require [ttt.board :as board]
            [ttt.player :as player]))

(defn computer-spot
  [board-length]
  (rand-int board-length))

(declare negamax)

; get collection of values for node (state of board)
(defn scores
  [board current-player opponent depth]
  (let [spots (board/available-spots board)
        new-boards (map #(board/move board current-player %) spots)]
    (map #(- (negamax % opponent current-player (inc depth))) new-boards)))

; if game is over, return board analysis
; else get the max from scores
(defn negamax
  [board current-player opponent depth]
  (if (board/game-over? board)
    (* (player/player-value current-player)
       (board/board-analysis board current-player opponent depth))
    (apply max (scores board current-player opponent depth))))

(def negamax (memoize negamax))

; get best move
(defn best-move
  [board current-player opponent depth]
  (let [spots (board/available-spots board)
        scores (scores board current-player opponent depth)
        max-value (negamax board current-player opponent depth)
        move (.indexOf scores max-value)]
    (nth spots move)))


; (comment
;   negamax (board player-value alpha beta depth)
;
;   if game is over or depth >= max-depth
;     (player-value * board-analysis)
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
