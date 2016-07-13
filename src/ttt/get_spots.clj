(ns ttt.get-spots
  (:require [ttt.helpers :as helpers]
            [ttt.negamax :as negamax]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.prompt :as prompt]
            [ttt.board :as board]))

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player params]
  (view/print-message messenger/choose-a-number)
  (let [input (prompt/prompt :number)]
    (if (helpers/is-int? input)
      (let [valid-number (helpers/input-to-number input)]
        (if (board/is-valid-move? (:board params) valid-number)
          valid-number
          (do
            (view/print-message (messenger/not-a-valid-move valid-number))
            (view/print-message (messenger/stringify-board (:board params)))
            (recur player params))))
      (do
        (view/print-message (messenger/not-a-valid-number input))
        (view/print-message (messenger/stringify-board (:board params)))
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
