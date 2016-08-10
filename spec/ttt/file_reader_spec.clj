(ns ttt.file-reader-spec
  (:require [speclj.core :refer :all]
            [clojure.java.io :as io]
            [ttt.file-reader :as file-reader]
            [ttt.board :as board]))

(describe "read-file"

    (with file-content (file-reader/read-file "hh.json" (io/file "test-files")))

    (it "returns a collection"
      (should (coll? @file-content)))

    (it "returns a collection of length equals 3"
      (should= 3 (count @file-content)))

    (it "returns a map that has key 'board'"
      (should (@file-content "board")))

    (it "returns a map that has key current-player"
      (should (@file-content "current-player")))

    (it "returns a map with nested keys current-player.role"
      (should (get-in @file-content ["current-player" "role"])))

    (it "returns a map with nested keys current-player.marker"
      (should (get-in @file-content ["current-player" "marker"])))

    (it "returns a map with key 'opponent'"
      (should (@file-content "opponent")))

    (it "returns a map with nested keys opponent.role"
      (should (get-in @file-content ["opponent" "role"])))

    (it "returns a map with nested keys opponent.marker"
      (should (get-in @file-content ["opponent" "role"]))))

(describe "convert-to-board-data"

  (with _ board/empty-spot)

  (it "takes a string representing an empty spot returns empty-spot keyword"
    (should= @_ (file-reader/convert-to-board-data "_")))

  (it "takes a string representing a marker and returns it"
    (should= "x" (file-reader/convert-to-board-data "x"))))

(describe "build-board-from-file"

  (with _ board/empty-spot)

  (it "returns an empty board if board from file only have empty spots"
    (should= (board/new-board)
             (file-reader/build-board-from-file ["_" "_" "_"
                                                 "_" "_" "_"
                                                 "_" "_" "_"])))

  (it "returns a board containing players markers and empty spots"
    (should= ["x" @_ "o" @_ @_ @_ @_ @_ "x"]
             (file-reader/build-board-from-file ["x" "_" "o" "_" "_" "_" "_" "_" "x"]))))

(describe "build-player-from-file"

  (with player {"role" "hard-computer" "marker" {"symbol" "o" "color" "green"}})

  (it "turns a map with string keys into a map with keywords keys"
    (should= {:role "hard-computer" :marker {:symbol "o" :color "green"} }
             (file-reader/build-player-from-file @player))))

(describe "saved-data"

  (it "returns a map with :current-player-data key"
    (should ((file-reader/saved-data "hh.json" (io/file "test-files"))
            :current-player-data)))

  (it "returns a map with :opponent-data key"
    (should ((file-reader/saved-data "hh.json" (io/file "test-files"))
            :opponent-data)))

  (it "returns a map with :board key"
    (should ((file-reader/saved-data "hh.json" (io/file "test-files"))
            :board-data))))

(describe "files"
  (it "returns an empty collection if directory has not files"
    (should (empty? (file-reader/files (io/file "test-files/empty-dir")))))

  (it "returns a collection with file objects"
      (should= 2 (count (file-reader/files (io/file "test-files"))))))

(describe "filenames"

  (it "returns a collection containing only the names of the files in a directory"
    (should (some #{"hh.json" "hchc.json"}
                  (file-reader/filenames (file-reader/files (io/file "test-files")))))))

(describe "names"

  (with list-of-files ["abc.json" "def.json" "something-something.json"])

  (it "returns a collection of strings"
    (should (and (coll? (file-reader/names @list-of-files))
                 (every? string? @list-of-files))))

  (it "returns a collection that contains 'abc'"
    (should (some #{"abc"} (file-reader/names @list-of-files))))

  (it "returns a collection that contains 'something-something'"
    (should (some #{"something-something"} (file-reader/names @list-of-files))))

  (it "returns a collections of strings that do not have file extension"
    (should= [] (filter #(re-find #"\." %) (file-reader/names @list-of-files)))))

(describe "is-there-any-file?"
  (it "returns false if there are not any files in default directory"
    (should-not (file-reader/is-there-any-file? (io/file "test-files/empty-dir"))))

  (it "returns true if there are files in default directory"
    (should (file-reader/is-there-any-file? (io/file "test-files")))))
