(ns ttt.get-spots)

(defmulti select-spot (fn [player & params] (:role player)))
