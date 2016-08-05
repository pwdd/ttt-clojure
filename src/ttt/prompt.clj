(ns ttt.prompt
  (:require [clojure.string :as string]
            [ttt.helpers :as helpers]
            [ttt.view :as view]
            [ttt.input-validation :as input-validation]
            [ttt.messenger :as messenger]
            [ttt.get-spots :refer [select-spot]]))

(defn prompt
  [clean-input msg]
  (view/print-message msg)
  (view/centralize-cursor)
  (let [input (clean-input (read-line))]
    (view/clear-screen)
    input))

(defn get-marker
  [{:keys [msg opponent-marker] :or {opponent-marker ""}}]
  (let [marker (prompt string/trim msg)]
    (if (input-validation/is-valid-marker? marker opponent-marker)
      marker
      (do
        (view/print-message (messenger/invalid-marker-msg marker opponent-marker))
        (recur {:msg msg :opponent-marker opponent-marker})))))

(defn get-role
  [marker]
  (let [input (prompt helpers/clean-string (messenger/ask-role-msg marker))]
    (if (input-validation/is-valid-role-selection? input)
      input
      (do
        (view/print-message messenger/invalid-role-options-msg)
        (recur marker)))))

(defn get-player-attributes
  [{:keys [msg opponent-marker] :or {opponent-marker ""}}]
  (let [marker (get-marker {:msg msg :opponent-marker opponent-marker})
        role (get-role marker)]
    {:marker marker :role role}))

(defn get-new-or-saved
  []
  (let [type-of-game (prompt string/trim messenger/new-or-saved-msg)]
    (if (input-validation/is-valid-new-or-saved? type-of-game)
      type-of-game
      (do
        (view/print-message messenger/default-invalid-input)
        (recur)))))

(defn choose-a-file
  [filenames]
  (let [chosen-file (prompt helpers/clean-string (messenger/list-saved-files filenames))]
    (if (input-validation/is-valid-filename? chosen-file filenames)
      chosen-file
      (do
        (view/print-message messenger/default-invalid-input)
        (recur filenames)))))

(defmethod select-spot :human
  [player params]
  (let [input (prompt string/trim messenger/choose-a-number)
        board (:board params)]
    (if (input-validation/is-valid-move-input? board input)
      (helpers/input-to-number input)
      (do
        (view/print-message (messenger/board-after-invalid-input board input))
        (recur player params)))))
