(ns ttt.prompt-spec
  (:require [speclj.core :refer :all]
            [ttt.prompt :refer :all]))

(describe "prompt"
  (context ":default"
    (it "returns the user input when prompted to enter a marker"
      (should= "x" (with-in-str "x" (prompt :marker))))
    (it "returns the user input when prompted to enter a number"
      (should= "1" (with-in-str "1" (prompt :number))))
    (it "trims out whitespaces from input"
      (should= "x" (with-in-str "  x " (prompt :marker))))
  (context ":role"
    (it "returns the user input when prompted to enter a player role"
        (should= "h" (with-in-str "h" (prompt :role))))
      (it "trims out whitespaces from input"
        (should= "h" (with-in-str "  h " (prompt :role))))
      (it "turns input into lowercase string"
        (should= "h" (with-in-str "H" (prompt :role)))))))

(describe "get-marker"
  (it "returns a player's marker if input is valid"
    (should= "x" (with-in-str "x" (get-marker { :msg "select marker" }))))
  (it "recurs and keep asking for input until it is valid"
    (should= "x" (with-in-str "1\n#\n x" (get-marker { :msg "recur" }))))
  (it "recurs and asks for new input if marker is being used by the first player"
    (should= "o" (with-in-str "x\no"
                   (get-marker { :msg "recur" :opponent-marker "x" })))))

(describe "get-role"
  (it "returns a player's role if input is valid"
    (should= "h" (with-in-str "h" (get-role { :msg "select role" }))))
  (it "recurs and keep asking for input until it is valid"
    (should= "ec" (with-in-str "1\n#\n x\nec" (get-role { :msg "recur" })))))

(describe "get-player-attributes"
  (let [attributes (with-in-str "x\nh"
                     (get-player-attributes { :msg "a player" }))]
    (it "returns a map with a key :marker"
      (should (:marker attributes)))
    (it "returns a map with a key :role"
      (should (:role attributes)))
    (it "is a map with the first input associate with :marker key"
      (should= "x" (:marker attributes)))
    (it "is a map with the second input associate with :role key"
      (should= "h" (:role attributes)))))
