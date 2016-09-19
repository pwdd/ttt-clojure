(ns ttt.boards.writer
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [ttt.boards.structure :as boards-structure]))

(def directory (io/file "boards"))

(defn generate-boards
  [all-boards]
  (into {} (map-indexed vector (map boards-structure/board-unit all-boards))))

(defn write-boards
  [all-boards]
  (json/write-str (generate-boards all-boards)))

(defn create-boards-file
  [directory filename all-boards]
  (spit (str directory "/" filename ".json")
        (write-boards all-boards)))
