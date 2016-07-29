(ns ttt.game-spec
  (:require [speclj.core :refer :all]
            [ttt.game :refer :all]
            [clojure.java.io :as io]
            [ttt.file-reader :as file-reader])
  (:import [ttt.game Game]))

(describe "game-type"
  (it "returns :human-x-human if game is human vs human"
    (should= :human-x-human (game-type :human :human)))
  (it "returns :hard-x-hard if game is hard-computer vs hard-computer"
    (should= :hard-x-hard (game-type :hard-computer :hard-computer)))
  (it "returns :easy-x-easy if game is easy-computer vs easy-computer"
    (should= :easy-x-easy (game-type :easy-computer :easy-computer)))
  (it "returns :hard-x-human if game is human vs hard-computer"
    (should= :hard-x-human (game-type :human :hard-computer)))
  (it "returns :hard-x-human if game is hard-computer vs human"
    (should= :hard-x-human (game-type :hard-computer :human)))
  (it "returns :easy-x-human if game is human vs easy-computer"
    (should= :easy-x-human (game-type :human :easy-computer)))
  (it "returns :easy-x-human if game is easy-computer vs human"
    (should= :easy-x-human (game-type :easy-computer :human)))
  (it "returns :easy-x-hard if game is easy-computer vs hard-computer"
    (should= :easy-x-hard (game-type :easy-computer :hard-computer)))
  (it "returns :easy-x-hard if game is hard-computer vs easy-computer"
    (should= :easy-x-hard (game-type :hard-computer :easy-computer))))

(describe "game-players-roles"
  (it "returns :same-player-roles if players are both :human"
    (should= :same-player-roles (game-players-roles :human :human)))
  (it "returns :same-player-roles if player are both :easy-computer"
    (should= :same-player-roles (game-players-roles :easy-computer
                                                    :easy-computer)))
  (it "returns :computer-x-human if first player is human and second an AI"
    (should= :computer-x-human (game-players-roles :human :easy-computer)))
  (it "returns :computer-x-human if first player is AI and second is human"
    (should= :computer-x-human (game-players-roles :hard-computer :human)))
  (it "returns :computer-x-computer if both players are AI"
    (should= :computer-x-computer (game-players-roles :hard-computer :easy-computer))))

(describe "create-game"
  (it "returns an instance of a Game"
    (should (instance? Game (create-game :human :easy-computer))))
  (it "returns a Game with a type"
    (should (:type (create-game :human :hard-computer))))
  (it "returns a Game with player-roles attribute"
    (should (:player-roles (create-game :human :hard-computer)))))

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

(describe "human-makes-first-move?"
  (it "returns false if player is not :human"
    (should-not (human-makes-first-move? true :easy-computer)))
  (it "returns false if it is not the first move of the game"
    (should-not (human-makes-first-move? false :human)))
  (it "returns true if it is the first move and player is :human"
    (should (human-makes-first-move? true :human))))
