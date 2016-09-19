(ns ttt.boards.generator
  (:require [ttt.boards.board :as board]
            [ttt.evaluate-game :as evaluate-game]))

(def empty-spot board/empty-spot)
(def current-player {:role :hard-computer :marker {:token :x}})
(def opponent {:role :easy-computer :marker {:token :o}})

(defn- markers-to-token
  [marker]
  (if (= marker board/empty-spot)
    marker
    (:token marker)))

(defn count-markers
  [board first-marker second-marker]
  (let [new-board (mapv markers-to-token board)
        markers-frequency (frequencies new-board)]
    (cond
      (nil? (first-marker markers-frequency))
        (assoc markers-frequency first-marker 0)
      (nil? (second-marker markers-frequency))
        (assoc markers-frequency second-marker 0)
      :else
        markers-frequency)))

(defn get-current-player
  [board first-token second-token]
  (let [markers-frequency (count-markers board first-token second-token)]
    (cond
      (board/is-board-empty? board) first-token
      (= (first-token markers-frequency)
         (second-token markers-frequency))
        first-token
      (> (first-token markers-frequency)
         (second-token markers-frequency))
        second-token
      :else
        first-token)))

(defn possible-moves
  [board token]
  (let [spots (board/available-spots board)]
    (if (map? token)
      (map #(board/move board % token) spots)
      (map #(board/move board % {:token token}) spots))))

(defn- initial-boards
  [empty-board]
  (concat [empty-board]
          (possible-moves empty-board (:marker current-player))
          (possible-moves empty-board (:marker opponent))))

(defn generate-next-boards
  [boards]
  (mapcat #(possible-moves % (get-current-player %
                                                 (get-in current-player [:marker :token])
                                                 (get-in opponent [:marker :token])))
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

(defn filter-game-over
  [all-boards]
  (filter #(not (evaluate-game/game-over? %)) all-boards))


