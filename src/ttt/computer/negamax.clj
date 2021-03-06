(ns ttt.computer.negamax
  (:require [ttt.board :as board]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.get-spots :refer [select-spot]]
            [ttt.player :as player]
            [ttt.helpers :as helpers]
            [ttt.rules :as rules]))

(def start-depth 0)
(def max-depth 10)
(def limit-depth 100)

(defn board-analysis
  [board current-player-marker opponent-marker depth]
  (let [winner (evaluate-game/winner-marker board)]
    (condp = winner
      current-player-marker (- limit-depth depth)
      opponent-marker (- depth limit-depth)
      0)))

(defn- create-next-boards
  [board available-spots current-player-marker]
  (map #(board/move board % current-player-marker) available-spots))

(declare negamax)

(defn scores
  [board current-player-marker opponent-marker depth]
  (let [spots (board/available-spots board)
        new-boards (create-next-boards board spots current-player-marker)]
    (map #(- (negamax %
                      opponent-marker
                      current-player-marker
                      (inc depth)))
         new-boards)))

(defn negamax-score
  [board current-player-marker opponent-marker depth]
  (if (or (evaluate-game/game-over? board)
          (>= depth max-depth))
    (board-analysis board current-player-marker opponent-marker depth)
    (apply max (scores board
                       current-player-marker
                       opponent-marker
                       depth))))

(def negamax (memoize negamax-score))

(def medium-board-length 16)
(def large-board-length 25)
(def start-medium-board (- medium-board-length 10))
(def start-large-board (- large-board-length 14))

(defn- medium-board?
  [board]
  (= (count board) medium-board-length))

(defn- large-board?
  [board]
  (= (count board) large-board-length))

(defn- alternative-board?
  [board]
  (or (medium-board? board) (large-board? board)))

(defn- number-of-available-spots
  [board]
  (count (board/available-spots board)))

(defn- first-moves-alternative-board?
  [board]
  (if (medium-board? board)
    (> (number-of-available-spots board) start-medium-board)
    (> (number-of-available-spots board) start-large-board)))

(defn- starting-game-with-alternative-board?
  [board]
  (and (alternative-board? board) (first-moves-alternative-board? board)))

(defmethod select-spot :hard-computer
  [game-params]
  (let [{board :board
         current-player :current-player
         opponent :opponent
         depth :depth} game-params]
  (cond
    (board/is-board-empty? board) (rules/place-in-the-center board)
    (starting-game-with-alternative-board? board)
      (rules/play-based-on-rules game-params)
    :else
      (let [spots (board/available-spots board)
           scores (scores board
                          (:marker current-player)
                          (:marker opponent)
                          depth)
           max-value (apply max scores)
           best (.indexOf scores max-value)]
        (nth spots best)))))
