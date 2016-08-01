(ns ttt.player
  (:require [ttt.helpers :as helpers]
            [ttt.input-validation :as input-validation]))

(defrecord Player [marker role ai value])

(defn marker
  [player]
  (:marker player))
; I don't think these wrappers are useful. They swap coupling you code to the name of a key to
; coupling your code to the name for a function. I'd consider those equivalent, and so you should
; stick to the simpler solution of not having these wrappers.

(defn value
  [player]
  (:value player))
; I don't think anything uses value.

(defn is-ai?
  [player]
  (:ai player))
; You might as well change the key to `:is-ai` if you think that is the clearer name. Better still,
; you could just make a function that can figure this out based on the `:role` attribute of a
; player, and get rid of this attribute entirely.

(defn role
  [player]
  (:role player))

(defn make-player
  [player-params]
  (cond
    (= (role player-params) :human)
       (->Player (marker player-params) (role player-params) false -1)
    (= (role player-params) :easy-computer)
       (->Player (marker player-params) (role player-params) true -1)
    :else
      (->Player (marker player-params) (role player-params) true 1)))

(defn define-player
  [attributes]
  (cond
    (input-validation/is-acceptable-as-human-player? (role attributes))
      (make-player { :marker (marker attributes)
                     :role :human })
    (input-validation/is-acceptable-as-easy-computer? (role attributes))
      (make-player { :marker (marker attributes)
                     :role :easy-computer })
    :else
      (make-player { :marker (marker attributes)
                     :role :hard-computer })))
; This validation of player role probably is better grouped along with other input validation code.
