(ns ttt.animated-start
  (:require [clojure.string :as string]
            [ttt.colors :as colors]
            [ttt.view :as view]
            [ttt.get-spots :as spots]
            [ttt.negamax :as negamax]
            [ttt.board :as board]
            [ttt.messenger :as messenger]
            [ttt.evaluate-game :as evaluate-game]))

(def initial-ttt-color :yellow)

(defn- timed-same-line-string
  [string time]
    (print string) (flush) (Thread/sleep time))

(defn animate-ttt
  [time]
  (let [spaces (view/padding-spaces (count "Tic Tac Toe") view/half-screen-width)
        ttt (map #(colors/colorize initial-ttt-color %)
                 [(str spaces "Tic") " Tac ""Toe"])]
    (doall (map #(timed-same-line-string % time) ttt))))

(defn end-animated-board
  [board]
  (let [first-wait 500 second-wait 1300 third-wait 800]
    (view/print-message (messenger/stringify-board board))
    (println "")
    (Thread/sleep first-wait)
    (animate-ttt third-wait)
    (Thread/sleep second-wait)
    (view/clear-screen)))

(defn animated-board
  [game board current-player opponent]
  (let [spot (spots/select-spot current-player
                                {:board board
                                 :current-player current-player
                                 :opponent opponent
                                 :depth negamax/start-depth
                                 :board-length board/board-length})
        game-board (board/move board spot (:marker current-player))
        board-time 300]
    (view/print-message (messenger/stringify-board board))
    (view/make-board-disappear (:role current-player) board-time)
    (if (evaluate-game/game-over? game-board)
      (end-animated-board game-board)
      (recur game game-board opponent current-player))))
