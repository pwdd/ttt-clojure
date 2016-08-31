(ns ttt.evaluate-game
  (:require [ttt.player :as player]
            [ttt.board :as board]))

(defn winner-marker
  [board]
  (if-let [combo (board/winning-combo board)]
    (board (combo 0))))

(defn winner-player
  [board first-player second-player]
  (let [winner-marker (winner-marker board)]
    (if (= winner-marker (:marker first-player))
      first-player
      second-player)))

(defn winner-role
  [board first-player second-player]
  (:role (winner-player board first-player second-player)))

(defn is-winner-ai?
 [board first-player second-player]
 (player/is-ai? (winner-role board first-player second-player)))

(defn draw?
  [board]
  (and (board/is-board-full? board)
       (not (winner-marker board))))

(defn game-over?
  [board]
  (or (draw? board)
      (not (nil? (board/winning-combo board)))))
