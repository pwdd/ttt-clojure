(ns ttt.player
  (:require [ttt.input-validation :as input-validation]))

(defrecord Player [marker role])

(defn is-ai?
  [player-role]
  (not (= :human player-role)))

(defn define-player
  [attributes]
  (cond
    (input-validation/is-acceptable-as-human-player? (:role attributes))
      (map->Player { :marker (:marker attributes)
                     :role :human })
    (input-validation/is-acceptable-as-easy-computer? (:role attributes))
      (map->Player { :marker (:marker attributes)
                     :role :easy-computer })
    :else
      (map->Player { :marker (:marker attributes)
                     :role :hard-computer })))
