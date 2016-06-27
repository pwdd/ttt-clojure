(ns ttt.spots
  (:require [ttt.helpers :as helpers]
            [ttt.negamax :as negamax]
            [ttt.messenger :as messenger]
            [ttt.board :as board]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Multimethod: select-spot   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player params]
  (messenger/print-message messenger/choose-a-number)
  (let [input (messenger/ask-user-number)]
    (if (helpers/is-int? input)
      (let [valid-number (helpers/input-to-number input)]
        (if (board/is-valid-move? (:board params) valid-number)
          valid-number
          (do
            (messenger/print-message (messenger/not-a-valid-move valid-number))
            (recur player params))))
      (do
        (messenger/print-message (messenger/not-a-valid-number input))
        (recur player params)))))

(defmethod select-spot :easy-computer
  [player params]
  (rand-nth (board/available-spots (:board params))))

(defmethod select-spot :hard-computer
  [player params]
  (negamax/best-move (:game params)
                     (:board params)
                     (:current-player params)
                     (:opponent params)
                     (:depth params)))
