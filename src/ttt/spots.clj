(ns ttt.spots
  (:require [ttt.helpers :as helpers]
            [ttt.negamax :as negamax]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Multimethod: select-spot   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player params]
  (let [input (helpers/clean-string (read-line))]
    (if (helpers/is-int? input)
      (helpers/input-to-number input)
      (recur player params))))

(defmethod select-spot :easy-computer
  [player params]
  (rand-int (:board-length params)))

(defmethod select-spot :hard-computer
  [player params]
  (negamax/best-move (:board params)
                     (:current-player params)
                     (:opponent params)
                     (:depth params)))
