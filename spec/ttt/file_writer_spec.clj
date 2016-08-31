(ns ttt.file-writer-spec
  (:require [speclj.core :refer :all]
            [ttt.file-writer :as file-writer]
            [ttt.board :as board]
            [ttt.file-reader :as file-reader]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(describe "write-board"
  (with _ board/empty-spot)

  (it "converts an empty board into valid JSON key/pair format"
    (should= "\"board\": [\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\"]"
             (file-writer/write-board (board/new-board 3))))

  (it "converts a board with markers into a valid JSON key/pair format"
    (should= "\"board\": [\"_\",\"x\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"o\"]"
             (file-writer/write-board [@_ "x" @_
                                       @_  @_ @_
                                       @_  @_ "o"]))))

(describe "write-players"
  (it "adds current player marker and role to respective key"
    (should= "\"current-player\": {\"marker\":{\"token\":\"x\",\"color\":\"green\"},\"role\":\"human\"}"
             (file-writer/write-players "current-player" { :marker {:token "x" :color "green"}
                                                           :role :human })))

  (it "adds opponent marker and role to respective key"
    (should= "\"opponent\": {\"marker\":{\"token\":\"o\",\"color\":\"green\"},\"role\":\"hard-computer\"}"
             (file-writer/write-players "opponent" { :marker {:token "o" :color "green"}
                                                     :role :hard-computer }))))

(describe "write-json"
  (it "returns a map in valid JSON format"
    (should= (str "{\"board\": [\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\",\"_\"],"
                  "\"current-player\": {\"marker\":{\"token\":\"x\",\"color\":\"green\"},\"role\":\"human\"},"
                  "\"opponent\": {\"marker\":{\"token\":\"o\",\"color\":\"green\"},\"role\":\"easy-computer\"}}")
             (file-writer/write-json {:board (board/new-board 3)
                                      :current-player {:marker {:token "x" :color :green} :role "human"}
                                      :opponent {:marker {:token "o" :color :green} :role "easy-computer"}}))))

(describe "create-game-file"

  (with directory (io/file "test-files"))
  (with filename "test-game")
  (with game-data {:board (board/new-board 3)
                   :current-player {:marker {:token "x" :color :green} :role "human"}
                   :opponent {:marker {:token "o" :color :green} :role "easy-computer"}})

  (it "write JSON data to a file"
    (file-writer/create-game-file @directory @filename @game-data)
    (should (some #{"test-game.json"}
                  (file-reader/filenames (file-reader/files @directory))))
    (io/delete-file (str @directory "/" @filename ".json"))))
