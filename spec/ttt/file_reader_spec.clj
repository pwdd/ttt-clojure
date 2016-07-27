(ns ttt.file-reader-spec
  (:require [speclj.core :refer :all]
            [clojure.java.io :as io]
            [ttt.file-reader :refer :all]
            [ttt.board :as board]))

(describe "read-file"
  (around [it]
    (with-redefs [directory (io/file "test-files")]
      (it)))
    (with file-content (read-file "hh.json"))
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

(describe "data-from-file"
  (around [it]
    (with-redefs [directory (io/file "test-files")]
      (it)))
  (with json-data (read-file "hh.json"))
  (it "returns the array representing the board"
    (should= ["x" "-" "o"
              "-" "-" "-"
              "-" "-" "-"] (data-from-file @json-data "board")))
  (it "returns the map representing the current-player"
    (should= { "role" "human" "marker" "x" }
             (data-from-file @json-data "current-player")))
  (it "returns the map representing the opponent"
    (should= { "marker" "o" "role" "human" }
             (data-from-file @json-data "opponent"))))

(describe "convert-to-board-data"
  (with _ board/empty-spot)
  (it "takes a string representing an empty spot returns empty-spot keyword"
    (should= @_ (convert-to-board-data "-")))
  (it "takes a string representing a marker and returns it"
    (should= "x" (convert-to-board-data "x"))))

(describe "build-board-from-file"
  (with _ board/empty-spot)
  (it "returns an empty board if board from file only have empty spots"
    (should= (board/new-board) (build-board-from-file ["-" "-" "-"
                                                       "-" "-" "-"
                                                       "-" "-" "-"])))
  (it "returns a board containing players markers and empty spots"
    (should= ["x" @_ "o"
              @_ @_ @_
              @_ @_ "x"] (build-board-from-file ["x" "-" "o"
                                                 "-" "-" "-"
                                                 "-" "-" "x"]))))

(describe "build-player-from-file"
  (with player { "role" "hard-computer" "marker" "o" })
  (it "turns a map with string keys into a map with keywords keys"
    (should= { :marker "o" :role "hard-computer" }
             (build-player-from-file @player))))

(describe "saved-data"
  (around [it]
    (with-redefs [directory (io/file "test-files")]
      (it)))
  (it "returns a map with :current-player-data key"
    (should ((saved-data "hh.json") :current-player-data)))
  (it "returns a map with :opponent-data key"
    (should ((saved-data "hh.json") :opponent-data)))
  (it "returns a map with :board key"
    (should ((saved-data "hh.json") :board-data))))

(describe "files"
  (it "returns an empty collection if directory has not files"
    (with-redefs [directory (io/file "test-files/empty-dir")]
      (should (empty? (files)))))
  (it "returns a collection with file objects"
    (with-redefs [directory (io/file "test-files")]
      (should= 2 (count (files))))))

(describe "filenames"
  (around [it]
    (with-redefs [directory (io/file "test-files")]
      (it)))
  (it "returns a collection containing only the names of the files in a directory"
    (should (some #{"hh.json" "hchc.json"} (filenames (files))))))

(describe "names"
  (around [it]
    (with-redefs [directory (io/file "test-files")]
      (it)))
  (it "returns a collection containing only filenames without extension"
    (should (some #{"hh" "hchc"} (names (filenames (files)))))))

(describe "is-there-any-file?"
  (it "returns false if there are not any files in default directory"
    (with-redefs [directory (io/file "test-files/empty-dir")]
      (should-not (is-there-any-file?))))
  (it "returns true if there are files in default directory"
    (should (is-there-any-file?))))
