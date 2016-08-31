 (ns ttt.choices
   (:require [ttt.input-validation :as input-validation]
             [ttt.game-loop :as game-loop]
             [ttt.file-reader :as file-reader]
             [ttt.view :as view]))

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
  (view/clear-and-quit))

(defn not-spot-input
  [input player params]
  (cond 
    (input-validation/save? input)
      (save-and-exit file-reader/directory
                     (enter-a-file-name (file-reader/list-all-files file-reader/directory))
                     {:board (:board params)
                     :current-player player
                     :opponent (:opponent params)})
    (input-validation/quit?) (view/clear-and-quit)
    :else
      (game-loop/game-loop {:game (:game params)
                            :board []
                            :current-player (:current-player params)
                            :opponent (:opponent params)
                            :saved (:saved params)
                            :first-screen true
                            :board-size (:board-size params)})))
