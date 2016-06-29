(ns ttt.player)

(defrecord Player [marker role ai value])

(defn player-marker
  [player]
  (:marker player))

(defn player-value
  [player]
  (:value player))

(defn is-ai?
  [player]
  (:ai player))

(defn player-role
  [player]
  (:role player))

(defn make-player
  [params]
  (if (= (:role params) :human)
    (->Player (:marker params) (:role params) false -1)
    (->Player (:marker params) (:role params) true 1)))
