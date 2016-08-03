(ns ttt.file-reader
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [ttt.board :as board]))

(def directory (io/file "saved"))

(defn read-file
  [filename]
  (json/read-str (slurp (str directory "/" filename))))

(defn convert-to-board-data
  [string]
  (if (= string "-")
    board/empty-spot
    string))

(defn build-board-from-file
  [board-from-file]
  (mapv #(convert-to-board-data %) board-from-file))

(defn build-player-from-file
  [player-from-file]
  (into {}
    (for [[k v] player-from-file]
      [(keyword k) v])))

(defn build-from-file
  [field-name]
  (if (= field-name "board")
    build-board-from-file
    build-player-from-file))

(defn saved-data
  [filename]
  (let [file-data (read-file filename)]
    (zipmap [:current-player-data :opponent-data :board-data]
            (mapv #((build-from-file %) (file-data %))
            ["current-player" "opponent" "board"]))))

(defn files
  []
  (filter #(.isFile %) (file-seq directory)))

(defn filenames
  [files]
  (map #(.getName %) files))

(defn names
  [filenames]
  (map #(subs % 0 (.indexOf % ".")) filenames))

(defn is-there-any-file?
  []
  (not (empty? (files))))
