(ns ttt.prompt-spec
  (:require [speclj.core :refer :all]
            [ttt.prompt :refer :all]
            [ttt.helpers :as helpers]
            [clojure.string :as string]))

(describe "prompt"
  (around [it]
    (with-out-str (it)))
  (context "default behavior"
    (it "returns the user input when prompted to enter a marker"
      (should= "x" (with-in-str "x" (prompt string/trim "a marker"))))
    (it "returns the user input when prompted to enter a number"
      (should= "1" (with-in-str "1" (prompt string/trim "a number"))))
    (it "trims out whitespaces from input"
       (should= "x" (with-in-str "  x " (prompt string/trim "a marker")))))
  (context "getting player role"
    (it "returns the user input when prompted to enter a player role"
        (should= "h" (with-in-str "h" (prompt helpers/clean-string "a role"))))
    (it "turns input into lowercase string"
      (should= "h" (with-in-str "H" (prompt helpers/clean-string ""))))))

(describe "get-marker"
  (around [it]
    (with-out-str (it)))
  (it "returns a player's marker if input is valid"
    (should= "x" (with-in-str "x" (get-marker { :msg "select marker" }))))
  (it "recurs and keep asking for input until it is valid"
    (should= "x" (with-in-str "1\n#\n x" (get-marker { :msg "recur" }))))
  (it "recurs and asks for new input if marker is being used by the first player"
    (should= "o" (with-in-str "x\no"
                   (get-marker { :msg "recur" :opponent-marker "x" })))))

(describe "get-role"
  (around [it]
    (with-out-str (it)))
  (it "returns a player's role if input is valid"
    (should= "h" (with-in-str "h" (get-role { :msg "select role" }))))
  (it "recurs and keep asking for input until it is valid"
    (should= "ec" (with-in-str "1\n#\n x\nec" (get-role { :msg "recur" })))))

(describe "get-player-attributes"
  (around [it]
    (with-out-str (it)))
    (it "returns a map with keys :marker and :role"
      (should= { :role "h" :marker "x" }
               (with-in-str "x\nh" (get-player-attributes { :msg "" }))))
    (it "returns a map with the first input associate with :marker key"
      (should= "x" (:marker (with-in-str "x\nh" (get-player-attributes { :msg "" })))))
    (it "returns a map with the second input associate with :role key"
      (should= "h" (:role (with-in-str "x\nh" (get-player-attributes { :msg "" }))))))

(describe "get-new-or-saved"
  (around [it]
    (with-out-str (it)))
  (it "returns 1 if user chose saved game"
    (should= "1" (with-in-str "1" (get-new-or-saved))))
  (it "returns 2 if user chose new game"
    (should= "2" (with-in-str "2" (get-new-or-saved))))
  (it "recurs if input is neither '1' nor '2'"
    (should= "1" (with-in-str "3\n-1\na\n11\n1" (get-new-or-saved)))))
