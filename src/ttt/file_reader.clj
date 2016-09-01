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

(defn- keywordize
  [hashmap]
  (let [keys (keys hashmap)
        values (vals hashmap)]
    (zipmap (map keyword keys) (map keyword values))))

(defn convert-to-board-data
  [board-element]
  (if (= board-element "_")
    board/empty-spot
    (keywordize board-element)))

(defn- keywordize-game-element
  [value]
  (cond
    (vector? value) (convert-to-board-data value)
    (map? value) (keywordize value)
  :else
    (keyword value)))

(defn- build-game-data
  [json-map]
  (zipmap (map keyword (keys json-map))
          (map keywordize-game-element (vals json-map))))

(defn build-board-from-file
  [board-from-file]
  (mapv convert-to-board-data board-from-file))

(defn build-player-from-file
  [player-from-file]
  (build-game-data player-from-file))

(defn- build-from-file
  [field-name]
  (if (= field-name "board")
    build-board-from-file
    build-player-from-file))

(defn saved-data
  [filename directory]
  (let [file-data (read-file filename directory)]
    (zipmap saved-data-keys (mapv #((build-from-file %) (file-data %)) json-file-keys))))

(defn- is-json?
  [filename]
  (re-find #".*\.json" filename))

(defn files
  [directory]
  (let [all-files (filter #(.isFile %) (file-seq directory))]
    (filter #(is-json? (.getName %)) all-files)))

(defn filenames
  [files]
  (map #(.getName %) files))

(defn names
  [filenames]
  (map #(subs % 0 (.indexOf % ".")) filenames))

(defn is-there-any-file?
  [directory]
  (seq (files directory)))

(defn list-all-files
  [directory]
  (->> directory files filenames names))
