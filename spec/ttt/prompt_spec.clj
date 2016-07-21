(ns ttt.prompt-spec
  (:require [speclj.core :refer :all]
            [ttt.prompt :refer :all]
            [ttt.helpers :as helpers]
            [ttt.view :as view]
            [clojure.string :as string]))

(describe "prompt"
  (around [it]
    (with-redefs [view/centralize-cursor (fn [])
                  view/clear-screen (fn [])]))
  (it "returns the user input when prompted to enter a marker"
    (should= "x" (with-in-str "x" (prompt string/trim))))
  (it "returns the user input when prompted to enter a number"
    (should= "1" (with-in-str "1" (prompt string/trim))))
  (it "trims out whitespaces from input"
     (should= "x" (with-in-str "  x " (prompt string/trim))))
  (it "returns the user input when prompted to enter a player role"
      (should= "h" (with-in-str "h" (prompt helpers/clean-string))))
    (it "turns input into lowercase string"
      (should= "h" (with-in-str "H" (prompt helpers/clean-string)))))

(describe "get-marker"
  (around [it]
    (with-redefs [prompt (fn [_] _)]))
  (it "returns a player's marker if input is valid"
    (should= "x" (with-in-str "x" (get-marker { :msg "select marker" }))))
  (it "recurs and keep asking for input until it is valid"
    (should= "x" (with-in-str "1\n#\n x" (get-marker { :msg "recur" }))))
  (it "recurs and asks for new input if marker is being used by the first player"
    (should= "o" (with-in-str "x\no"
                   (get-marker { :msg "recur" :opponent-marker "x" })))))

(describe "get-role"
  (around [it]
    (with-redefs [prompt (fn [_] _)]))
  (it "returns a player's role if input is valid"
    (should= "h" (with-in-str "h" (get-role { :msg "select role" }))))
  (it "recurs and keep asking for input until it is valid"
    (should= "ec" (with-in-str "1\n#\n x\nec" (get-role { :msg "recur" })))))

(describe "get-player-attributes"
  (around [it]
    (with-redefs [get-marker (fn [_] _)
                  get-role (fn [_] _)]))
    (it "returns a map with a key :marker"
      (should (:marker (get-player-attributes { :msg "player" }))))
    (it "returns a map with a key :role"
      (should (:role (get-player-attributes { :msg "player" }))))
    (it "is a map with the first input associate with :marker key"
      (should= "x" (:marker (get-player-attributes { :msg "player" }))))
    (it "is a map with the second input associate with :role key"
      (should= "h" (:role (get-player-attributes { :msg "player" })))))
