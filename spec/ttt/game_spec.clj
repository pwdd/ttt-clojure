(ns ttt.game-spec
  (:require [speclj.core :refer :all]
            [ttt.game :refer :all]
            [ttt.player :refer [make-player]])
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
