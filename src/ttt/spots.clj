(ns ttt.spots
  (:require [ttt.helpers :as helpers]
            [ttt.negamax :as negamax]
            [ttt.messenger :as messenger]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Multimethod: select-spot   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player & params]
  (messenger/print-message messenger/choose-a-number)
  (let [input (messenger/ask-user-number)]
    (if (helpers/is-int? input)
      (helpers/input-to-number input)
      (do
        (messenger/print-message (messenger/not-a-number input))
        (recur player params)))))

(defmethod select-spot :easy-computer
  [player params]
  (rand-int (:board-length params)))

(defmethod select-spot :hard-computer
  [player params]
  (negamax/best-move (:game params)
                     (:board params)
                     (:current-player params)
                     (:opponent params)
                     (:depth params)))
