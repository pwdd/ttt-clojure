(ns ttt.player
  (:require [ttt.helpers :as helpers]))

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
    (= (:role player-params) :human)
       (->Player (:marker player-params) (:role player-params) false -1)
    (= (:role player-params) :easy-computer)
       (->Player (:marker player-params) (:role player-params) true -1)
    :else
      (->Player (:marker player-params) (:role player-params) true 1)))
