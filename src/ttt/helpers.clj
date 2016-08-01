(ns ttt.helpers
  (:require [clojure.string :as string]))

(defn clean-string
  [str]
  (string/lower-case (string/trim str)))

(defn input-to-number
  [user-input]
  (dec (Integer/parseInt (clojure.string/trim user-input))))

(defn in-range?
  [idx limit]
  (and (>= idx 0) (< idx limit)))

(defn stringify-role
  [player-role]
  (if (= :human player-role)
    "human"
    (let [role (name player-role)
         limit (.indexOf role "-")]
      (subs role 0 limit))))
; This doesn't strike me as a general-purpose method, and should probably go in game.clj

(defn write-game-type
  [first-name second-name]
  (keyword (string/join "-x-"(sort [first-name second-name]))))
; This one too

(defn clean-filenames
  [filenames]
  (map #(subs % 0 (.indexOf % ".")) filenames))
