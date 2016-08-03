(ns ttt.view
  (:require [clojure.string :as string]
            [clojure.java.shell :as sh]))

(defn get-console-width
  []
  (->> (sh/sh "/bin/sh" "-c" "stty -a < /dev/tty")
        :out (re-find #"(\d+) columns") second))

(defn get-half-screen-width
  []
  (if (re-find #"Win(.*)" (System/getProperty "os.name"))
    40
    (/ (Integer/parseInt (get-console-width)) 2)))

(def half-screen-width (get-half-screen-width))
(def center-of-screen "[8;6H")
(def height 24)
(def final-msg-lines 9)
(def flush-down
  (string/join (repeat (/ (- height final-msg-lines) 2) "\n")))

(defn clear-screen
  []
  (let [escape (char 27)]
    (print (str escape "[2J" escape center-of-screen))))

(defn number-of-spaces
  [message-length]
  (- half-screen-width (/ message-length 2)))

(defn padding-spaces
  [message-length]
  (string/join (repeat (number-of-spaces message-length) " ")))

(defn add-padding-spaces
  [message]
  (->> message
       string/split-lines
       (map #(str (padding-spaces (count %)) %))
       (string/join "\n")
       (str "\n")))

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
