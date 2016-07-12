(ns ttt.mocks
  (:require [ttt.player :as player]))

(defn mock-make-player
  [player-params]
  (player/make-player player-params))
