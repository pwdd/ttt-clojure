(ns ttt.player
  (:require [ttt.input-validation :as input-validation]
            [ttt.marker :as marker])
  (:import [ttt.marker Marker]))

(defrecord Player [marker role start-game])

(defn is-ai?
  [player-role]
  (not= :human player-role))

(defn started-game?
  [player-start-game]
  (= player-start-game :first))

(defn- make-player
  [marker color role start-game]
  (if (map? marker)
    (map->Player {:marker (marker/make-marker marker) 
                  :role role 
                  :start-game start-game})
    (map->Player {:marker (marker/make-marker {:token marker :color color})
                  :role role
                  :start-game start-game})))

(defn define-player
  [attributes player-color start-game]
  (cond
    (input-validation/is-acceptable-as-human-player? (:role attributes))
      (make-player (:marker attributes) player-color :human start-game)
    (input-validation/is-acceptable-as-easy-computer? (:role attributes))
      (make-player (:marker attributes) player-color :easy-computer start-game)
    (input-validation/is-acceptable-as-medium-computer? (:role attributes))
      (make-player (:marker attributes) player-color :medium-computer start-game)
    :else
      (make-player (:marker attributes) player-color :hard-computer start-game)))
