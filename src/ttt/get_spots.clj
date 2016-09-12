(ns ttt.get-spots)

(defmulti select-spot (fn [game-params] (get-in game-params [:current-player :role])))
