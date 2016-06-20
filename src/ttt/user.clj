(ns ttt.user
  (:require [ttt.helpers :as helpers]
            [ttt.board :as board]
            [ttt.messenger :as messenger]))

(defn get-human-spot
  []
  (println messenger/choose-a-number)
  (let [input (clojure.string/trim (read-line))]
    (if (helpers/is-int? input)
      (helpers/input-to-number input)
      (recur))))
