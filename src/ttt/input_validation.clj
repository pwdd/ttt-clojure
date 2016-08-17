(ns ttt.input-validation
  (:require [ttt.helpers :as helpers]
            [ttt.board :as board]))

(def acceptable-human-player
  #{"h" "human" "hum"})

(def acceptable-easy-computer
  #{"ec" "easy computer" "easycomputer" "easy" "easy-computer"})

(def acceptable-hard-computer
  #{"hc" "hard computer" "hardcomputer" "hard" "difficult" "hard-computer"})

(def saved-game-option "1")
(def new-game-option "2")

(def overwrite-file "1")
(def dont-overwrite-file "2")

(def save-valid-input "save")

(defn is-acceptable-as-human-player?
  [role]
  (contains? acceptable-human-player role))

(defn is-acceptable-as-easy-computer?
  [role]
  (contains? acceptable-easy-computer role))

(defn is-acceptable-as-hard-computer?
  [role]
  (contains? acceptable-hard-computer role))

(defn is-valid-role-selection?
 [role]
 (or (is-acceptable-as-human-player? role)
     (is-acceptable-as-easy-computer? role)
     (is-acceptable-as-hard-computer? role)))

(defn is-valid-marker?
 [input opponent-token]
 (and (= (count input) 1)
      (re-matches #"^[a-zA-Z]$" input)
      (not= (keyword input) opponent-token)))

(defn is-int?
  [input]
  (try
    (Integer/parseInt (helpers/clean-string input))
    true
  (catch Exception e false)))

(defn is-valid-move-input?
  [board input]
  (and (is-int? input)
       (board/is-valid-move? board (helpers/input-to-number input))))

(defn is-valid-new-or-saved?
  [input]
  (or (= input saved-game-option) (= input new-game-option)))

(defn is-valid-filename?
  [input filenames]
  (some #{input} filenames))

(defn save?
  [input]
  (= input save-valid-input))

(def valid-board-dimensions ["3" "4" "5"])

(defn is-valid-board-dimension?
  [input]
  (some #{input} valid-board-dimensions))
