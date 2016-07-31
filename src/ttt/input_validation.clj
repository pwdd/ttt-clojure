(ns ttt.input-validation
  (:require [ttt.helpers :as helpers]
            [ttt.board :as board]))

(def acceptable-human-player
  #{ "h" "human" "hum" })

(def acceptable-easy-computer
  #{ "ec" "easy computer" "easycomputer" "easy" "easy-computer"})

(def acceptable-hard-computer
  #{ "hc" "hard computer" "hardcomputer" "hard" "difficult" "hard-computer"})

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
 [input opponent-marker]
 (and (= (count input) 1)
      (re-matches #"^[a-zA-Z]$" input)
      (not (= input opponent-marker))))

(defn is-int?
  [user-input]
  (try
    (Integer/parseInt (helpers/clean-string user-input))
    true
  (catch Exception e false
    )))

(defn is-valid-move-input?
  [board user-input]
  (and (is-int? user-input)
       (board/is-valid-move? board (helpers/input-to-number user-input))))

(defn is-valid-new-or-saved?
  [user-input]
  (or (= user-input "1") (= user-input "2")))

(defn is-valid-filename?
  [user-input filenames]
  (some #(= user-input %) filenames))
