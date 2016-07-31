(ns ttt.get-spots
  (:require [clojure.string :as string]
            [ttt.helpers :as helpers]
            [ttt.negamax :as negamax]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.prompt :as prompt]
            [ttt.board :as board]
            [ttt.input-validation :as input-validation]))

(defmulti select-spot (fn [player & params] (:role player)))

(defmethod select-spot :human
  [player params]
  (let [input (prompt/prompt string/trim messenger/choose-a-number)]
    (if (input-validation/is-valid-move-input? (:board params) input)
      (helpers/input-to-number input)
      (do
        (view/print-message (messenger/wrong-number-msg (:board params) input))
        (view/print-message (messenger/stringify-board (:board params)))
        (recur player params)))))

(defmethod select-spot :easy-computer
  [player params]
  (rand-nth (board/available-spots (:board params))))

(defmethod select-spot :hard-computer
  [player params]
  (negamax/best-move (:board params)
                     (:current-player params)
                     (:opponent params)
                     (:depth params)))
