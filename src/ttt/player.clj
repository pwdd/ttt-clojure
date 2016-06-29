(ns ttt.player)

(defrecord Player [marker ai value])

(defn player-marker
  [player]
  (:marker player))

(defn player-value
  [player]
  (:value player))

(defn is-ai?
  [player]
  (:ai player))

(defn make-player
  [params]
  (if (:ai params)
    (->Player (:marker params) (:ai params) 1)
    (->Player (:marker params) (:ai params) -1)))
