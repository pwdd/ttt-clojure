(ns ttt.get-spots
  (:require [ttt.helpers :as helpers]
            [ttt.negamax :as negamax]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.prompt :as prompt]
            [ttt.board :as board]
            [ttt.input-validation :as input-validation]))

(defn wrong-number-msg
  [board input]
  (if (integer? input)
    (view/print-message (messenger/not-a-valid-move input))
    (view/print-message (messenger/not-a-valid-number input)))
    (view/print-message (messenger/stringify-board board)))

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player params]
  (view/print-message messenger/choose-a-number)
  (let [input (prompt/prompt :number)]
    (if (input-validation/is-int? input)
      (let [valid-number (helpers/input-to-number input)]
        (if (board/is-valid-move? (:board params) valid-number)
          valid-number
          (do
            (wrong-number-msg (:board params) valid-number)
            (recur player params))))
      (do
        (wrong-number-msg (:board params) input)
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
