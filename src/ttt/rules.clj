(ns ttt.rules
  (:require [ttt.player :as player]
            [ttt.board :as board]))

(defn winner-marker
  [board]
  (if (board/winning-combo board)
    (let [combo (board/winning-combo board)]
       (board (combo 0)))))

(defn winner-player
  [board first-player second-player]
  (let [winner (winner-marker board)]
    (cond
      (= (player/marker first-player) winner) first-player
      (= (player/marker second-player) winner) second-player
      :else
        false)))

(defn is-winner-ai?
 [board first-player second-player]
 (let [winner (winner-player board first-player second-player)]
   (player/is-ai? winner)))

(defn draw?
  [board]
  (and (board/is-board-full? board)
       (not (winner-marker board))))

(defn game-over?
  [board]
  (or (draw? board)
      (not (nil? (board/winning-combo board)))))
