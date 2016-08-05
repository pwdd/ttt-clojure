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
        (view/print-message
          (messenger/invalid-marker-msg marker opponent-marker))
        (recur {:msg msg
                 :opponent-marker opponent-marker})))))

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
  (view/print-message "These are the saved files:")
  (view/print-message (messenger/display-files-list filenames))
  (let [chosen-file (prompt helpers/clean-string messenger/choose-a-file-msg)]
    (if (input-validation/is-valid-filename? chosen-file filenames)
      chosen-file
      (do
        (view/print-message messenger/default-invalid-input)
        (recur filenames)))))

(defmethod select-spot :human
  [player params]
  (let [input (prompt string/trim messenger/choose-a-number)]
    (if (input-validation/is-valid-move-input? (:board params) input)
      (helpers/input-to-number input)
      (do
        (view/print-message (messenger/wrong-number-msg (:board params) input))
        (view/print-message (messenger/stringify-board (:board params)))
        (recur player params)))))
