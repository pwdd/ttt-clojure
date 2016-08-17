(ns ttt.prompt
  (:require [clojure.string :as string]
            [ttt.helpers :as helpers]
            [ttt.view :as view]
            [ttt.input-validation :as input-validation]
            [ttt.messenger :as messenger]
            [ttt.file-writer :as file-writer]
            [ttt.file-reader :as file-reader]
            [ttt.get-spots :refer [select-spot]]
            [ttt.player :as player]))

(defn prompt
  [clean-input msg]
  (view/print-message msg)
  (view/centralize-cursor)
  (let [input (clean-input (read-line))]
    (view/clear-screen)
    input))

(defn get-marker
  [{:keys [msg color opponent-token] :or {opponent-token :0}}]
  (let [marker (prompt string/trim msg)]
    (if (input-validation/is-valid-marker? marker opponent-token)
      {:token (keyword marker) :color color}
      (do
        (view/print-message (messenger/invalid-marker-msg marker opponent-token))
        (recur {:msg msg :color color :opponent-token opponent-token})))))

(defn get-role
  [marker]
  (let [input (prompt helpers/clean-string (messenger/ask-role-msg marker))]
    (if (input-validation/is-valid-role-selection? input)
      input
      (do
        (view/print-message messenger/invalid-role-options-msg)
        (recur marker)))))

(defn get-player-attributes
  [{:keys [msg color opponent-token] :or {opponent-marker :0}}]
  (let [marker (get-marker {:msg msg :color color :opponent-token opponent-token})
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

(defn file-exist-msg
  [first-loop]
  (if (true? first-loop)
    (view/print-message messenger/file-already-exists-msg)))

(declare enter-a-file-name)

(defn overwrite-file
  [input existing-files first-loop]
  (file-exist-msg first-loop)
  (let [overwrite (prompt helpers/clean-string messenger/overwrite-file-option)]
    (cond
      (= overwrite input-validation/overwrite-file) input
      (= overwrite input-validation/dont-overwrite-file)
        (enter-a-file-name existing-files)
      :else
        (do
          (view/print-message messenger/default-invalid-input)
          (recur input existing-files false)))))

(defn enter-a-file-name
  [existing-files]
  (let [input (prompt helpers/clean-string messenger/give-file-name)]
    (if-not (input-validation/is-valid-filename? input existing-files)
      input
      (overwrite-file input existing-files true))))

(defn save-and-exit
  [directory filename {:keys [board current-player opponent]}]
  (file-writer/create-game-file directory
                                filename
                                {:board board
                                 :current-player current-player
                                 :opponent opponent})
  (view/print-message messenger/game-saved)
  (System/exit 0))

(defmethod select-spot :human
  [player params]
  (let [input (prompt helpers/clean-string messenger/number-or-save)
        board (:board params)]
    (cond
      (input-validation/save? input)
        (save-and-exit file-reader/directory
                       (enter-a-file-name (file-reader/list-all-files file-reader/directory))
                       {:board (:board params)
                       :current-player player
                       :opponent (:opponent params)})
      (input-validation/is-valid-move-input? board input)
        (helpers/input-to-number input)
      :else
        (do
          (view/print-message (messenger/board-after-invalid-input board input))
          (recur player params)))))

(defn get-board-size
  []
  (let [input (prompt string/trim messenger/choose-board-size)]
    (if (input-validation/is-valid-board-size? input)
      input
      (recur))))
