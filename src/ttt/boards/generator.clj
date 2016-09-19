(ns ttt.boards.generator
  (:require [ttt.boards.board :as board]
            [ttt.evaluate-game :as evaluate-game]))

(def empty-spot :_)
(def current-player :x)
(def opponent :o)

(defn- count-markers
  [board first-marker second-marker]
  (let [markers-frequency (frequencies board)]
    (cond
      (nil? (first-marker markers-frequency))
        (assoc markers-frequency first-marker 0)
      (nil? (second-marker markers-frequency))
        (assoc markers-frequency second-marker 0)
      :else
        markers-frequency)))

(defn- get-current-player
  [board first-marker second-marker]
  (let [markers-frequency (count-markers board first-marker second-marker)]
    (cond
      (board/is-board-empty? board) first-marker
      (= (first-marker markers-frequency)
         (second-marker markers-frequency))
        first-marker
      (> (first-marker markers-frequency)
         (second-marker markers-frequency))
        second-marker
      :else
        first-marker)))

(defn possible-moves
  [board marker]
  (->> (board/available-spots board)
       (map #(board/move board % marker))))

(defn- initial-boards
  [empty-board]
  (concat [empty-board]
          (possible-moves empty-board current-player)
          (possible-moves empty-board opponent)))

(defn- generate-next-boards
  [boards]
  (mapcat #(possible-moves % (get-current-player % current-player opponent))
          boards))

(defn all-boards
  [size]
  (let [empty-board (board/new-board size)]
    (loop [boards (initial-boards empty-board)
           base-board empty-board
           spots (board/available-spots base-board)]
      (if (= 1 (count spots))
        (distinct boards)
        (let [new-boards (generate-next-boards boards)]
          (recur (concat boards new-boards)
                 (last new-boards)
                 (board/available-spots (last new-boards))))))))

(defn filter-out-win-board
  [all-boards]
  (filter #(not (evaluate-game/game-over? %)) all-boards))


