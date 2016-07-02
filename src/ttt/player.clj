(ns ttt.player
  (:require [ttt.helpers :as helpers]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Player record and getters   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

;;;;;;;;;;;;;;;;;;;;;;;
;;   Create Player   ;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn make-player
  [params]
  (cond
    (= (:role params) :human)
       (->Player (:marker params) (:role params) false -1)
    (= (:role params) :easy-computer)
       (->Player (:marker params) (:role params) true -1)
    :else
      (->Player (:marker params) (:role params) true 1)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Multimethods: select-spot   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player]
  (let [input (helpers/clean-string (read-line))]
    (if (helpers/is-int? input)
      (helpers/input-to-number input)
      (recur player))))

(defmethod select-spot :easy-computer
  [player params]
  (rand-int (:board-length params)))

(defmethod select-spot :hard-computer
  [player params]
  (negamax/best-move (:board params)
                     (:current-player params)
                     (:opponent params)
                     (:depth params)))
