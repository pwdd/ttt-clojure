(ns ttt.asker
  (:require [ttt.messenger :as messenger]
            [ttt.helpers :as helpers]))

(defn valid-selection?
  [input]
  (or (= input "h")
     (= input "human")
     (= input "c")
     (= input "computer")))

(defn who-plays
  []
  (println messenger/h-or-c)
  (let [input (helpers/clean-string (read-line))]
    (if (valid-selection? input)
      (str (nth input 0))
      (recur))))

(defn define-player
  [marker & [args]]
  (if args
    (println args))
  (let [type (who-plays)]
    (if (= type "h")
      {:type :human :marker marker}
      {:type :computer :marker marker})))

(defn get-user-spot
  []
  (println messenger/choose-a-number)
  (let [input (read-line)]
    (if (helpers/is-int? input)
      (helpers/input-to-number input)
      (recur))))
