(ns ttt.negamax
  (:require [ttt.board :as board]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.get-spots :refer [select-spot]]
            [ttt.player :as player]
            [ttt.helpers :as helpers]))

(def start-depth 0)

(defn board-analysis
  [board current-player-marker opponent-marker depth]
  (let [winner (evaluate-game/winner-marker board)]
    (condp = winner 
      current-player-marker (- 10 depth)
      opponent-marker (- depth 10) 
      0)))

(defn create-next-boards
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
          (>= depth 4))
    (board-analysis board current-player-marker opponent-marker depth)
    (apply max (scores board 
                       current-player-marker 
                       opponent-marker 
                       depth ))))

(def negamax (memoize negamax-score))

(def medium-board-length 16)
(def large-board-length 25)
(def start-medium-board (- medium-board-length 5))
(def start-large-board (- large-board-length 7))

(defn- medium-board?
  [board]
  (= (count board) medium-board-length))

(defn- large-board?
  [board]
  (= (count board) large-board-length))

(defn- alternative-board?
  [board]
  (or (medium-board? board) (large-board? board)))

(defn- first-moves-alternative-board?
  [board]
  (if (medium-board? board)
    (> (count (board/available-spots board)) start-medium-board)
    (> (count (board/available-spots board)) start-large-board)))

(defmethod select-spot :hard-computer
  [player params]
  (cond 
    (board/is-board-empty? (:board params)) 4
    (and (alternative-board? (:board params)) 
         (first-moves-alternative-board? (:board params))) (helpers/random-move (:board params))
    :else
    (let [spots (board/available-spots (:board params))
         scores (scores (:board params)
                        (player/marker (:current-player params))
                        (player/marker (:opponent params))
                        (:depth params))
         max-value (apply max scores)
         best (.indexOf scores max-value)]
      (nth spots best))))
