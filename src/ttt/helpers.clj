(ns ttt.helpers
  (:require [clojure.string :as string]))

(defn is-windows-os?
  []
  (re-find #"Win(.*)" (System/getProperty "os.name")))

(defn clean-string
  [str]
  (string/lower-case (string/trim str)))

(defn input-to-number
  [input]
  (dec (Integer/parseInt (string/trim input))))

(defn in-range?
  [idx limit]
  (and (>= idx 0) (< idx limit)))

(defn random-move
  [available-spots]
  (rand-nth available-spots))

(defn get-keys-by-value
  [map-collection value]
  (keep #(when (= (val %) value) (key %)) map-collection))

(defn remove-color
  [string]
  (let [escape (char 27)
        re-color-code #"\[\d?;?\d*m"]
    (loop [string string
           code-list (re-seq re-color-code string)]
      (if (empty? code-list)
        string
        (recur (string/replace string (str escape (first code-list)) "")
               (rest code-list))))))
