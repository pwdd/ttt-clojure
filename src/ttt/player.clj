(ns ttt.player
  (:require [ttt.helpers :as helpers]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;   Player record and getters   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord Player [marker role ai value])

(defn marker
  [player]
  (:marker player))

(defn value
  [player]
  (:value player))

(defn is-ai?
  [player]
  (:ai player))

(defn role
  [player]
  (:role player))

;;;;;;;;;;;;;;;;;;;;;;;
;;   Create Player   ;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn make-player
  [params]
  (cond
    (= (:role params) :human)
       (->Player (:marker params) (:role params) false -1)
    (= (:role params) :easy-computer)
       (->Player (:marker params) (:role params) true -1)
    :else
      (->Player (:marker params) (:role params) true 1)))
