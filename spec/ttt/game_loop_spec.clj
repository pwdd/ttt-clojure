(ns ttt.game-loop-spec
  (:require [speclj.core :refer :all]
            [ttt.game-loop :refer :all]
            [clojure.java.io :as io]
            [ttt.file-reader :as file-reader]
            [ttt.input-validation :as input-validation]))

(describe "game-setup"

  (context "setup-resumed-game"

    (around [it]
      (with-redefs [file-reader/directory (io/file "test-files")]
      (with-out-str (it))))

    (with data (with-in-str "hh" (game-setup "1")))

    (it "returns a map"
      (should (map? @data)))

    (it "returns a map with a key :game"
      (should (@data :game)))

    (it "returns a map with a key :board"
      (should (@data :board)))

    (it "returns a map with a key :current-player"
      (should (@data :current-player)))

    (it "returns a map with a key :opponent"
      (should (@data :opponent))))

  (context "setup-regular-game"

    (around [it]
      (with-out-str (it)))

    (with data (with-in-str "x\nec\no\nec" (game-setup "2" "first" "second")))

    (it "returns a map"
      (should (map? @data)))

    (it "returns a map with key :game"
      (should (@data :game)))

    (it "returns a map with a key :current-player"
      (should (@data :current-player)))

    (it "returns a map with a key :opponent"
      (should (@data :opponent)))))

(describe "game-selection"
  (around [it]
    (with-out-str (it)))

  (it "calls prompt to get option if game is new or saved if there is saved files"
    (with-redefs [file-reader/directory (io/file "test-files")]
      (should= input-validation/saved-game-option
               (with-in-str "1" (game-selection)))
      (should= input-validation/new-game-option
               (with-in-str "2" (game-selection)))))

  (it "returns default selection for new game if there is no saved files"
    (with-redefs [file-reader/directory (io/file "test-files/empty-dir")]
      (should= "2" (game-selection)))))

(describe "play"
  (it "runs the game"
    (should-not-throw (Exception.))))
