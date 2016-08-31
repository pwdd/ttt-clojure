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
(def welcome "Welcome to Tic Tac Toe")

(defn padded-welcome
  []
  (let [spaces (view/padding-spaces (count welcome) view/half-screen-width)]
    (str spaces "Welcome to ")))

(defn- timed-same-line-string
  [string time]
    (print string)
    (flush)
    (Thread/sleep time))

(defn animate-ttt
  [time]
  (let [ttt (map #(colors/colorize initial-ttt-color %) ["Tic" " Tac ""Toe"])]
    (doall (map #(timed-same-line-string % time) ttt))))

(defn end-animated-board
  [board]
  (let [short-wait 500 medium-wait 800 long-wait 1300]
    (view/print-message (messenger/stringify-board board))
    (println "")
    (Thread/sleep short-wait)
    (timed-same-line-string (padded-welcome) short-wait)
    (animate-ttt medium-wait)
    (Thread/sleep long-wait)
    (view/clear-screen)))

(defn animated-board
  [game board current-player opponent]
  (let [spot (spots/select-spot current-player
                                {:board board
                                 :current-player current-player
                                 :opponent opponent
                                 :depth negamax/start-depth
                                 :board-length board/board-length
                                 :alpha 100
                                 :beta -100})
        game-board (board/move board spot (:marker current-player))
        board-time 300]
    (view/print-message (messenger/stringify-board board))
    (view/make-board-disappear (:role current-player) board-time)
    (if (evaluate-game/game-over? game-board)
      (end-animated-board game-board)
      (recur game game-board opponent current-player))))
