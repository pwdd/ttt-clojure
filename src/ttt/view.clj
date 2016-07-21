(ns ttt.view
  (:require [clojure.string :as string]))

(def half-screen-width 60)
(def center-of-screen "[8;6H")

(defn clear-screen
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) center-of-screen)))

(defn number-of-spaces
  [message-length]
  (let [half-message (int (Math/ceil (/ message-length 2.0)))]
    (- half-screen-width half-message)))

(defn padding-spaces
  [message-length]
  (string/join
    (repeat (number-of-spaces message-length) " ")))

(defn add-padding-spaces
  [message]
  (str "\n"
       (string/join "\n" (map
                        #(str (padding-spaces (count %)) %)
                        (string/split-lines message)))))

(defn print-message
  [msg]
  (println (add-padding-spaces msg)))

(defn centralize-cursor
  []
  (do (print (padding-spaces 0)) (flush)))

(defn make-board-disappear
  [player-role]
  (if (or (= :easy-computer player-role)
          (= :hard-computer player-role))
    (do (Thread/sleep 1000)
        (clear-screen))))
