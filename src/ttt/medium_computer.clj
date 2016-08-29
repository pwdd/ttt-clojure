(ns ttt.medium-computer
  (:require [ttt.get-spots :refer [select-spot]]
            [ttt.rules :as rules]
            [ttt.board :as board]
            [ttt.helpers :as helpers]))

(defn- owned
  [board current-player-marker opponent-marker]
  (rules/most-populated-owned-section board current-player-marker opponent-marker))

(defn- empty-section
  [board]
  (rules/is-there-empty-section? board))

(defmethod select-spot :medium-computer
  [player params]
  (let [board (:board params)
        current-player-marker (get-in params [:current-player :marker])
        opponent-marker (get-in params [:opponent :marker])
        board-size (board/board-size board)]

    (cond
      (rules/is-middle-the-best-move? board) (rules/place-in-the-middle board)
      (and (rules/is-board-with-one-move? board)
           (not (rules/is-middle-free? board)))
        (helpers/random-move (rules/available-spots-in-section
                               board 
                               (rules/corners board-size)))
      (rules/where-can-win board current-player-marker) 
        (rules/place-in-winning-spot board current-player-marker) 
      (rules/where-can-win board opponent-marker)
        (rules/place-in-winning-spot board opponent-marker)
      (not (empty? (rules/owned-sections board 
                                         current-player-marker 
                                         opponent-marker)))
        (helpers/random-move 
          (rules/available-spots-in-section board 
                                            (owned board 
                                                   current-player-marker 
                                                   opponent-marker)))
      (rules/is-there-empty-section? board)
        (helpers/random-move (empty-section board))
      :else
        (helpers/random-move (board/available-spots board)))))
 
