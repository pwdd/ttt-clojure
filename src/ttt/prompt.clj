(ns ttt.prompt
  (:require [ttt.helpers :as helpers]
            [ttt.view :as view]
            [ttt.input-validation :as input-validation]
            [ttt.messenger :as messenger]))

(defn prompt
  [clear-screen clean-input centralize-cursor ]
  (centralize-cursor)
  (let [role (clean-input (read-line))]
    (clear-screen)
    role))

(defn get-marker
  [{ :keys [msg opponent-marker] :or { opponent-marker "" } }]
  (view/print-message msg)
  (let [marker (prompt view/clear-screen clojure.string/trim view/centralize-cursor )]
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
  (let [input (prompt view/clear-screen helpers/clean-string view/centralize-cursor)]
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
