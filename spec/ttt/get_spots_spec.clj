(ns ttt.get-spots-spec
  (:require [speclj.core :refer :all]
            [ttt.get-spots :as spots]
            [ttt.board :as board]
            [ttt.negamax :as negamax]))

(describe "select-spot"

  (around [it]
    (with-out-str (it)))

  (with _ board/empty-spot)
  (with human {:role :human :marker {:symbol :x :color :blue}})
  (with easy-computer {:role :easy-computer :marker {:symbol :o :color :red}})
  (with hard-computer {:role :hard-computer :marker {:symbol :x :color :blue}})

  (context ":human"
    (it "returns an integer"
      (should= 0 (with-in-str "1" (spots/select-spot @human {:board (board/new-board)}))))

    (it "returns an integer that numeric string minus one"
      (should= 3 (with-in-str "4" (spots/select-spot @human {:board (board/new-board)})))))

  (context ":easy-computer"
    (with spots (board/available-spots [:x @_ :o @_ :o @_ @_ :x :o :o]))
    (it "returns a random index from the available-spots"
      (should (some #{(spots/select-spot @easy-computer {:board [:x @_ :o @_ :o @_ @_ :x :o]})}
                    @spots))))

  (context ":hard-computer"
    (it "blocks opponent from winning"
      (should= 2 (spots/select-spot @hard-computer
                                    {:board [:o :o @_
                                             :x @_ @_
                                             :x @_ @_]
                                     :current-player @hard-computer
                                     :opponent @easy-computer
                                     :depth negamax/start-depth})))

    (it "wins when it has the chance"
      (should= 5 (spots/select-spot @hard-computer
                                    {:board [:o :o @_
                                             :x :x @_
                                             @_ @_ @_]
                                     :current-player @hard-computer
                                     :opponent @easy-computer
                                     :depth negamax/start-depth})))

    (it "avoids situation in which opponent can win in two positions"
      (should (or (= 2 (spots/select-spot @hard-computer
                                          {:board [:x @_ @_
                                                   @_ :o @_
                                                   @_ @_ :o]
                                           :current-player @hard-computer
                                           :opponent @easy-computer
                                           :depth negamax/start-depth}))
                  (= 6 (spots/select-spot @hard-computer
                                          {:board [:x @_ @_
                                                   @_ :o @_
                                                   @_ @_ :o]
                                           :current-player @hard-computer
                                           :opponent @easy-computer
                                           :depth negamax/start-depth})))))))
