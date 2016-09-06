(ns ttt.get-spots-spec
  (:require [speclj.core :refer :all]
            [ttt.get-spots :as spots]
            [ttt.board :as board]
            [ttt.negamax :as negamax]
            [ttt.rules :as rules]))

(describe "select-spot"

  (around [it]
    (with-out-str (it)))

  (with _ board/empty-spot)
  (with x {:token :x :color :red})
  (with o {:token :o :color :blue})
  (with m {:token :m :color :yellow})
  (with human {:role :human :marker @x})
  (with easy-computer {:role :easy-computer :marker @o})
  (with hard-computer {:role :hard-computer :marker @x})
  (with medium-computer {:role :medium-computer :marker @m})

  (context ":human"
    (it "returns an integer"
      (should= 0 (with-in-str "1" (spots/select-spot @human
                                                     {:board (board/new-board 3)}))))

    (it "returns an integer that numeric string minus one"
      (should= 3 (with-in-str "4" (spots/select-spot @human
                                                     {:board (board/new-board 3)})))))

  (context ":easy-computer"
    (with spots (board/available-spots [@x @_ @o @_ @o @_ @_ @x @o @o]))
    (it "returns a random index from the available-spots"
      (should (some #{(spots/select-spot @easy-computer
                                         {:board [@x @_ @o @_ @o @_ @_ @x @o]})}
                    @spots))))

  (context ":hard-computer"
    (it "blocks opponent from winning"
      (should= 2 (spots/select-spot @hard-computer
                                    {:board [@o @o @_
                                             @x @_ @_
                                             @x @_ @_]
                                     :current-player @hard-computer
                                     :opponent @easy-computer
                                     :depth negamax/start-depth})))

    (it "wins when it has the chance"
      (should= 5 (spots/select-spot @hard-computer
                                    {:board [@o @o @_
                                             @x @x @_
                                             @_ @_ @_]
                                     :current-player @hard-computer
                                     :opponent @easy-computer
                                     :depth negamax/start-depth})))

    (it "avoids situation in which opponent can win in two positions"
      (should (or (= 2 (spots/select-spot @hard-computer
                                          {:board [@x @_ @_
                                                   @_ @o @_
                                                   @_ @_ @o]
                                           :current-player @hard-computer
                                           :opponent @easy-computer
                                           :depth negamax/start-depth}))
                  (= 6 (spots/select-spot @hard-computer
                                          {:board [@x @_ @_
                                                   @_ @o @_
                                                   @_ @_ @o]
                                           :current-player @hard-computer
                                           :opponent @easy-computer
                                           :depth negamax/start-depth })))))

    (it "avoids opponent win in a 4x4 board"
      (should= 3 (spots/select-spot @hard-computer
                                   {:board [@o @o @o @_
                                            @_ @_ @_ @_
                                            @_ @_ @x @_
                                            @x @x @_ @_]
                                    :current-player @hard-computer
                                    :opponent @easy-computer
                                    :depth negamax/start-depth})))

    (it "wins when it can in a 4x4 board"
      (should= 7 (spots/select-spot @hard-computer
                                    {:board [@o @o @o @_
                                             @x @x @x @_
                                             @_ @_ @_ @_
                                             @_ @_ @_ @_]
                                     :current-player @hard-computer
                                     :opponent @easy-computer
                                     :depth negamax/start-depth}))))
  (context ":medium-computer"
    (it "places marker in the middle if board is empty"
      (should= 6 (spots/select-spot @medium-computer
                                     {:board (board/new-board 4)
                                     :current-player @medium-computer
                                     :opponent @easy-computer})))

    (it "places marker in the middle if opponent made only one move"
      (should= 4 (spots/select-spot @medium-computer
                                    {:board [@o @_ @_
                                             @_ @_ @_
                                             @_ @_ @_]
                                     :current-player @medium-computer
                                     :opponent @easy-computer})))

    (it "wins when possible"
      (should= 2 (spots/select-spot @medium-computer
                                    {:board [@m @m @_
                                             @o @_ @_
                                             @o @_ @_]
                                     :current-player @medium-computer
                                     :opponent @easy-computer})))

    (it "blocks opponent from winning"
      (should= 8 (spots/select-spot @medium-computer
                                    {:board [@o @m @_
                                             @_ @o @_
                                             @_ @m @_]
                                     :current-player @medium-computer
                                     :opponent @easy-computer})))

   (it "fills in a board section if there is no blocking or winning situation"
      (let [solution (spots/select-spot @medium-computer
                                        {:board [@o @o @_ @_
                                                 @m @_ @_ @_
                                                 @o @_ @_ @_
                                                 @_ @_ @_ @_]
                                         :current-player @medium-computer
                                         :opponent @easy-computer})]
        (should (some #{solution} [5 6 7]))))

   (it "picks a corner"
     (let [solution (spots/select-spot @medium-computer
                                       {:board [@_ @_ @_
                                                @_ @o @_
                                                @_ @_ @_]
                                        :current-player @medium-computer
                                        :opponent @easy-computer})]
       (should (some #{solution} (rules/corners 3)))))

   (it "picks an empty section"
      (let [solution (spots/select-spot @medium-computer
                                        {:board [@_ @o @_ @_
                                                 @o @m @_ @_
                                                 @m @o @o @_
                                                 @m @_ @o @_]
                                        :current-player @medium-computer
                                        :opponent @easy-computer})]
        (should (some #{solution} [3 7 11 15]))))))
