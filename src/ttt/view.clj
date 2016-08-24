(ns ttt.view
  (:require [clojure.string :as string]
            [clojure.java.shell :as sh]
            [ttt.colors :as colors]))

(defn get-console-width
  []
  (->> (sh/sh "/bin/sh" "-c" "stty -a < /dev/tty")
        :out (re-find #"(\d+) columns") second))

(defn get-half-screen-width
  []
  (if (re-find #"Win(.*)" (System/getProperty "os.name"))
    40
    (quot (Integer/parseInt (get-console-width)) 2)))

(def half-screen-width (get-half-screen-width))
(def center-of-screen "[8;6H")
(def height 24)
(def final-msg-lines 9)
(def flush-down (string/join (repeat (quot (- height final-msg-lines) 2) "\n")))

(defn clear-screen
  []
  (let [escape (char 27)]
    (print (str escape "[2J" escape center-of-screen))))

(defn number-of-spaces
  [message-length half-screen-width]
  (- half-screen-width (quot message-length 2)))

(def color-re #"\[\d?;?\d*m")

(defn color-code-list
  [message-string]
    (re-seq color-re message-string))

(defn color-code-length
  [message-string]
  (if-let [color-list (color-code-list message-string)]
    (+ (count color-list) (count (string/join color-list)))
    0))

(defn message-length
  [message]
  (- (count message) (color-code-length message)))

(defn padding-spaces
  [message-length half-screen-width]
  (string/join (repeat (number-of-spaces message-length half-screen-width) " ")))

(defn add-padding-spaces
  [message half-screen-width]
  (->> message
       string/split-lines
       (map #(str (padding-spaces (message-length %) half-screen-width) %))
       (string/join "\n")
       (str "\n")))

(defn print-message
  [msg]
  (println (str (:default colors/ansi-colors)
                (add-padding-spaces msg half-screen-width)
                (:default colors/ansi-colors))))

(defn centralize-cursor
  []
  (do (print (padding-spaces 0 half-screen-width)) (flush)))

(defn make-board-disappear
  [player-role time]
  (when (or (= :easy-computer player-role)
            (= :hard-computer player-role))
    (Thread/sleep time)
    (clear-screen)))
