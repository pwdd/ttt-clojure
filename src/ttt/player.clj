(ns ttt.player
  (:require [ttt.input-validation :as input-validation]))

(defrecord Player [marker role])

(defn is-ai?
  [player-role]
  (not (= :human player-role)))

(defn role
  [player]
  (:role player))

(defn marker
  [player]
  (:marker player))

(defn token
  [player]
  (get-in player [:marker :token]))

(defn color
  [player]
  (get-in player [:marker :color]))

(defn- make-player
  [marker color role]
  (if (map? marker)
    (map->Player {:marker marker :role role})
    (map->Player {:marker {:token marker :color color}
                  :role role})))

(defn define-player
  [attributes player-color]
  (cond
    (input-validation/is-acceptable-as-human-player? (:role attributes))
      (make-player (:marker attributes) player-color :human)
    (input-validation/is-acceptable-as-easy-computer? (:role attributes))
      (make-player (:marker attributes) player-color :easy-computer)
    :else
      (make-player (:marker attributes) player-color :hard-computer)))
