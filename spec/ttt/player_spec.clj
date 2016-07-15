(ns ttt.player-spec
  (:require [speclj.core :refer :all]
            [ttt.player :refer :all]
            [ttt.board :as board])
  (:import [ttt.player Player]))

(describe "marker"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns the marker associated with a human player"
      (should= :x (marker human)))
    (it "returns the marker associated with an easy-computer player"
      (should= :e (marker easy-computer)))
    (it "returns the marker associated with a hard-computer player"
      (should= :h (marker hard-computer)))))

(describe "value"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns the value associated with a human player"
      (should= -1 (value human)))
    (it "returns the value associated with an easy-computer player"
      (should= -1 (value easy-computer)))
    (it "returns the value associated with a hard-computer player"
      (should= 1 (value hard-computer)))))

(describe "is-ai?"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns true if player is easy-computer"
      (should (is-ai? easy-computer)))
    (it "returns true if player is hard-computer"
      (should (is-ai? hard-computer)))
    (it "returns false if player is not ai"
      (should-not (is-ai? human)))))

(describe "role"
  (let [human (make-player { :role :human :marker :x })
        easy-computer (make-player { :role :easy-computer :marker :e })
        hard-computer (make-player { :role :hard-computer :marker :h })]
    (it "returns :human if player role is human"
      (should= :human (role human)))
    (it "returns :easy-computer if player role is easy computer"
      (should= :easy-computer (role easy-computer)))
    (it "returns :hard-computer if player role is hard computer"
      (should= :hard-computer (role hard-computer)))))

(describe "make-player"
  (it "returns an instance of Player"
    (should (instance? Player (make-player { :role :easy-computer
                                             :marker "A" }))))
  (it "returns Player that has :ai attribute"
    (should= true (:ai (make-player { :role :easy-computer :marker "a" }))))
  (it "returns Player that has :value attribute"
    (should= -1 (:value (make-player { :role :easy-computer :marker "c" }))))
  (it "returns a Player that has a :role attribute"
    (should= :hard-computer
             (:role (make-player { :role :hard-computer :marker "a" })))))

(describe "winner-marker"
  (let [human (make-player { :marker :x :role :human })
       u (marker human)
       easy-computer (make-player { :role :easy-computer :marker :e })
       e (marker easy-computer)
       _ board/empty-spot
       empty-board (board/new-board)]
    (it "returns nil if board is empty"
      (should (nil? (winner-marker empty-board))))
    (it "returns nil if there is no winner"
      (should (nil? (winner-marker [u e u
                                    e u e
                                    e u e]))))
    (it "returns the winner marker if there is one on rows"
      (should= u (winner-marker [u u u
                                 _ _ e
                                 e e _])))
    (it "returns the winner marker if there is one in second row"
      (should= u (winner-marker [_ _ e
                                 u u u
                                 e e _])))
    (it "returns the winner marker if there is one in column"
      (should= u (winner-marker [u _ _
                                 u e e
                                 u _ _])))
    (it "returns the winner marker if there is one in the second column"
      (should= u (winner-marker [_ u _
                                e u e
                                e u _])))
    (it "returns the winner marker if there is one in a diagonal"
      (should= e (winner-marker [e u u
                                 u e _
                                 _ _ e])))
    (it "returns the winner marker if there is one in the other diagonal"
      (should= u (winner-marker [u _ u
                                 e u e
                                 u e _])))))

(describe "winner-player"
  (let [human (make-player { :marker :x :role :human })
        u (marker human)
        easy-computer (make-player { :role :easy-computer :marker :e })
        e (marker easy-computer)
        hard-computer (make-player { :role :hard-computer :marker :h })
        h (marker hard-computer)
        _ board/empty-spot]
    (it "returns human player if human won the game"
      (should= human (winner-player [u u u
                                     e e _
                                     e e _]
                                     easy-computer
                                     human)))
    (it "should not return easy-computer player if human won the game"
      (should-not (= easy-computer (winner-player [u u u
                                                   e e _
                                                   e e _]
                                                   easy-computer
                                                   human))))
    (it "returns easy-computer player if it won the game"
      (should= easy-computer  (winner-player [e u u
                                              e _ u
                                              e u _]
                                              easy-computer
                                              human)))
    (it "returns hard-computer player if it won the game"
      (should= hard-computer (winner-player [h h h
                                             e e _
                                             _ _ _]
                                             hard-computer
                                             easy-computer)))))

(describe "is-winner-ai?"
  (let [human (make-player { :marker :x :role :human })
        u (marker human)
        easy-computer (make-player { :role :easy-computer :marker :e })
        e (marker easy-computer)
        hard-computer (make-player { :role :hard-computer :marker :h })
        h (marker hard-computer)
        _ board/empty-spot]
    (it "returns false if winner has :ai attribute false"
      (should-not (is-winner-ai? [u e u
                                  e u e
                                  e e u]
                                  human
                                  easy-computer)))
    (it "returns true if winner is easy-computer"
      (should (is-winner-ai? [u u e
                              e e e
                              e e u]
                              human
                              easy-computer)))
    (it "returns true if winner is hard-computer"
      (should (is-winner-ai? [u u h
                              h h h
                              h h u]
                              human
                              hard-computer)))))

(describe "define-player"
  (let [human (make-player { :marker "x" :role :human })
        easy-computer (make-player { :role :easy-computer :marker "o" })
        hard-computer (make-player { :role :hard-computer :marker "h" })]
  (it "returns player with type 'human' and its marker"
    (should= human
             (define-player { :marker "x" :role "h" })))
  (it "returns player with type 'easy-computer' and its marker"
    (should= easy-computer
             (define-player { :marker "o" :role "ec" })))
  (it "returns player with type 'hard-computer' and its marker"
    (should= hard-computer
             (define-player { :marker "h" :role "hc" })))))
