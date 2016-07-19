(ns ttt.prompt
  (:require [clojure.string :as string]
            [ttt.helpers :as helpers]
            [ttt.view :as view]
            [ttt.input-validation :as input-validation]
            [ttt.messenger :as messenger]))

(defn prompt
  [clean-input]
  (view/centralize-cursor)
  (let [role (clean-input (read-line))]
    (view/clear-screen)
    role))

(defn get-marker
  [{ :keys [msg opponent-marker] :or { opponent-marker "" } }]
  (view/print-message msg)
  (let [marker (prompt string/trim)]
    (if (input-validation/is-valid-marker? marker opponent-marker)
      marker
      (do
        (view/print-message
          (messenger/invalid-marker-msg marker opponent-marker))
        (recur { :msg msg
                 :opponent-marker opponent-marker })))))

(defn get-role
  [marker]
  (view/print-message (messenger/ask-role-msg marker))
  (let [input (prompt helpers/clean-string)]
    (if (input-validation/is-valid-role-selection? input)
      input
      (do
        (view/print-message messenger/invalid-role-options-msg)
        (recur marker)))))

(defn get-player-attributes
  [{ :keys [msg opponent-marker] :or { opponent-marker "" } }]
  (let [marker (get-marker { :msg msg :opponent-marker opponent-marker })
        role (get-role marker)]
    { :marker marker :role role }))
