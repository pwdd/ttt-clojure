(ns ttt.get-spots-spec
  (:require [speclj.core :refer :all]
            [ttt.get-spots :refer :all]
            [ttt.board :as board]))

(describe "select-spot"

  (around [it]
    (with-out-str (it)))

  (with _ board/empty-spot)
  (with human {:role :human :marker :x})
  (with easy-computer {:role :easy-computer :marker :o})

  (context ":human"
    (it "returns an integer"
      (should= 0 (with-in-str "1" (select-spot @human {:board (board/new-board)}))))

    (it "returns an integer that numeric string minus one"
      (should= 3 (with-in-str "4" (select-spot @human {:board (board/new-board)})))))

  (context ":easy-computer"
    (with spots (board/available-spots [:x @_ :o @_ :o @_ @_ :x :o :o]))
    (it "returns a random index from the available-spots"
      (should (some #{(select-spot @easy-computer {:board [:x @_ :o @_ :o @_ @_ :x :o]})}
                    @spots)))))
