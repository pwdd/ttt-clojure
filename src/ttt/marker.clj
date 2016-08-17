(ns ttt.marker)

(defrecord Marker [token color])

(defn make-marker
  [{:keys [token color]}]
  (map->Marker {:token token :color color}))
