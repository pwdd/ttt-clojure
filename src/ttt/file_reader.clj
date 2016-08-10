(ns ttt.file-reader
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [ttt.board :as board]))

(def directory (io/file "saved"))
(def json-file-keys ["current-player" "opponent" "board"])
(def saved-data-keys [:current-player-data :opponent-data :board-data])

(defn read-file
  [filename directory]
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

(defn- build-from-file
  [field-name]
  (if (= field-name "board")
    build-board-from-file
    build-player-from-file))

(defn saved-data
  [filename directory]
  (let [file-data (read-file filename directory)]
    (zipmap saved-data-keys (mapv #((build-from-file %) (file-data %)) json-file-keys))))

(defn files
  [directory]
  (filter #(.isFile %) (file-seq directory)))

(defn filenames
  [files]
  (map #(.getName %) files))

(defn names
  [filenames]
  (map #(subs % 0 (.indexOf % ".")) filenames))

(defn is-there-any-file?
  [directory]
  (not (empty? (files directory))))
