(ns ttt.player
  (:require [ttt.helpers :as helpers]
            [ttt.board :as board]
            [ttt.input-validation :as input-validation]))

(defrecord Player [marker role ai value])

(defn marker
  [player]
  (:marker player))

(defn value
  [player]
  (:value player))

(defn is-ai?
  [player]
  (:ai player))

(defn role
  [player]
  (:role player))

(defn make-player
  [player-params]
  (cond
    (= (role player-params) :human)
       (->Player (marker player-params) (role player-params) false -1)
    (= (role player-params) :easy-computer)
       (->Player (marker player-params) (role player-params) true -1)
    :else
      (->Player (marker player-params) (role player-params) true 1)))

(defn winner-marker
  [board]
  (if (board/winning-combo board)
    (let [combo (board/winning-combo board)]
       (board (combo 0)))))

(defn winner-player
  [board first-player second-player]
  (let [winner (winner-marker board)]
    (cond
      (= (marker first-player) winner) first-player
      (= (marker second-player) winner) second-player
      :else
        false)))

(defn is-winner-ai?
 [board first-player second-player]
 (let [winner (winner-player board first-player second-player)]
   (is-ai? winner)))

(defn define-player
  [attributes]
  (cond
    (input-validation/is-acceptable-as-human-player? (role attributes))
      (make-player { :marker (marker attributes)
                     :role :human })
    (input-validation/is-acceptable-as-easy-computer? (role attributes))
      (make-player { :marker (marker attributes)
                     :role :easy-computer })
    :else
      (make-player { :marker (marker attributes)
                     :role :hard-computer })))
