(ns ttt.game-spec
  (:require [speclj.core :refer :all]
            [ttt.game :refer :all]
            [ttt.player :refer [make-player]])
  (:import [ttt.game Game]))

(def human (make-player {:marker "x" :role :human}))
(def easy-computer (make-player {:marker "o" :role :easy-computer}))
(def hard-computer (make-player {:marker "h" :role :hard-computer}))

(describe "game-type"
  (it "returns :human-x-human if game is human vs human"
    (should= :human-x-human (game-type human human)))
  (it "returns :hard-x-hard if game is hard-computer vs hard-computer"
    (should= :hard-x-hard (game-type hard-computer hard-computer)))
  (it "returns :easy-x-easy if game is easy-computer vs easy-computer"
    (should= :easy-x-easy (game-type easy-computer easy-computer)))
  (it "returns :hard-x-human if game is human vs hard-computer"
    (should= :hard-x-human (game-type human hard-computer)))
  (it "returns :hard-x-human if game is hard-computer vs human"
    (should= :hard-x-human (game-type hard-computer human)))
  (it "returns :easy-x-human if game is human vs easy-computer"
    (should= :easy-x-human (game-type human easy-computer)))
  (it "returns :easy-x-human if game is easy-computer vs human"
    (should= :easy-x-human (game-type easy-computer human)))
  (it "returns :easy-x-hard if game is easy-computer vs hard-computer"
    (should= :easy-x-hard (game-type easy-computer hard-computer)))
  (it "returns :easy-x-hard if game is hard-computer vs easy-computer"
    (should= :easy-x-hard (game-type hard-computer easy-computer))))

(describe "create-game"
  (it "returns an instance of a Game"
    (should (instance? Game (create-game human easy-computer))))
  (it "returns a Game with a type"
    (should (:type (create-game human hard-computer)))))

; (describe "define-player"
;   (it "returns player with type 'human' and its marker"
;     (should= human
;              (with-in-str "x\nh" (define-player { :msg "set player" }))))
;   (it "returns player with type 'easy-computer' and its marker"
;     (should= easy-computer
;              (with-in-str "o\nec" (define-player { :msg "set player" }))))
;   (it "returns player with type 'hard-computer' and its marker"
;     (should= hard-computer
;              (with-in-str "h\nhc" (define-player { :msg "set player" })))))
