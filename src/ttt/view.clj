(ns ttt.view
  (:require [clojure.string :as string]
            [clojure.java.shell :as sh]))

(defn get-console-width
  []
  (if (not (= "Windows" (System/getProperty "os.name")))
    (->> (sh/sh "/bin/sh" "-c" "stty -a < /dev/tty")
         :out (re-find #"(\d+) columns") second)
    120))

(def half-screen-width (/ (Integer/parseInt (get-console-width)) 2))
(def center-of-screen "[8;6H")
(def height 24)
(def final-msg-lines 9)
(def flush-down
  (string/join (repeat (/ (- height final-msg-lines) 2) "\n")))

(defn clear-screen
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) center-of-screen)))

(defn number-of-spaces
  [message-length]
  (- half-screen-width (/ message-length 2)))

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
