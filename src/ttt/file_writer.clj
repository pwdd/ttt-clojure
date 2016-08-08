(ns ttt.file-writer
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(defn write-board
  [board]
  (str "\"board\": " (json/write-str board)))

(defn write-players
  [player-turn player-data]
  (str "\"" player-turn "\": " (json/write-str player-data)))


(defn write-json
  [{:keys [board current-player opponent]}]
  (str "{"
       (write-board board)
       ","
       (write-players "current-player" current-player)
       ","
       (write-players "opponent" opponent)
       "}"))

(defn create-game-file
  [directory filename {:keys [board current-player opponent]}]
  (spit (str directory "/" filename ".json")
        (write-json {:board board :current-player current-player :opponent opponent})))

(defn save-and-exit
  [directory filename {:keys [board current-player opponent]}]
  (create-game-file directory
                    filename
                    {:board board :current-player current-player :opponent opponent})
  (System/exit 0))
