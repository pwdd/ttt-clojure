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
  (into { }
    (for [[k v] player-from-file]
      [(keyword k) v])))

(defn saved-data
  [filename]
  (let [file-data (read-file filename)]
    {
      :current-player-data
        (build-player-from-file (file-data "current-player"))
      :opponent-data
        (build-player-from-file (file-data "opponent"))
      :board-data
        (build-board-from-file (file-data "board"))
    }))

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
