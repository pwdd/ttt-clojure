(ns ttt.user
  (:require [ttt.helpers :as helpers]
            [ttt.messenger :as messenger]))

(defn get-user-spot
  []
  (println messenger/choose-a-number)
  (let [input (read-line)]
    (if (helpers/is-int? input)
      (helpers/input-to-number input)
      (recur))))
