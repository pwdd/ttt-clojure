(ns ttt.game-loop-spec
  (:require [speclj.core :refer :all]
            [ttt.game-loop :as game-loop]
            [ttt.player :as player]
            [clojure.java.io :as io]
            [ttt.file-reader :as file-reader]
            [ttt.input-validation :as input-validation]))

(describe "game-setup"

  (around [it]
    (with-out-str (it)))

  (context "setup-resumed-game"

    (with data (with-in-str "hh" (game-loop/game-setup "1" (io/file "test-files"))))

    (it "returns a map"
      (should (map? @data)))

    (it "returns a map with a key :game"
      (should (@data :game)))

    (it "returns a map with a key :board"
      (should (@data :board)))

    (it "returns a map with a key :current-player"
      (should (@data :current-player)))

    (it "returns a map with a key :current-player.marker holding a map with :token and :color"
      (should= {:token :x, :color :green}
               (:marker (@data :current-player))))

    (it "returns a map with a key :opponent"
      (should (@data :opponent))))

  (context "setup-regular-game"

    (around [it]
      (with-out-str (it)))

    (with data (with-in-str "3\nx\nec\no\nec"
                 (game-loop/game-setup "2" (io/file "test-files") "first" "second")))

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
    (should= input-validation/saved-game-option
             (with-in-str "1" (game-loop/game-selection (io/file "test-files"))))
    (should= input-validation/new-game-option
             (with-in-str "2" (game-loop/game-selection (io/file "test-files")))))

  (it "returns default selection for new game if there is no saved files"
      (should= "2" (game-loop/game-selection (io/file "test-files/empty-dir")))))

