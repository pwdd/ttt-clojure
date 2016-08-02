(ns ttt.helpers
  (:require [clojure.string :as string]))

(defn clean-string
  [str]
  (string/lower-case (string/trim str)))

(defn input-to-number
  [user-input]
  (dec (Integer/parseInt (string/trim user-input))))

(defn in-range?
  [idx limit]
  (and (>= idx 0) (< idx limit)))
