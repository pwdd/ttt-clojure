(ns ttt.game-loop-spec
  (:require [speclj.core :refer :all]
            [ttt.game-loop :refer :all]
            [clojure.java.io :as io]
            [ttt.file-reader :as file-reader]))

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

(describe "play"
  (it "runs the game"
    (should-not-throw (Exception.))))
