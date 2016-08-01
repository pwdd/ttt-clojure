(ns ttt.board
  (:require [ttt.helpers :as helpers]))

(def board-size 3)
(def board-length (* board-size board-size))
(def empty-spot :_)

(defn new-board
  []
  (vec (repeat board-length empty-spot)))
; You might consider changing your `board` data structure to include the data that you are currently
; defining separately (`board-size`, `empty-spot`, etc). This would make your app a little more
; flexible and a little cleaner I think.

(defn board-rows
  []
  (partition board-size (range board-length)))

(defn board-columns
  []
  (apply map vector (board-rows)))

(defn diagonals
  [start-column next-column]
  (loop [row 0
         column start-column
         diagonal []]
    (if (>= row board-size)
      diagonal
      (recur (inc row)
             (next-column column)
             (conj diagonal
               (get-in (mapv vec (board-rows)) [row column]))))))

(defn board-diagonals
  []
  [(diagonals 0 inc) (diagonals (dec board-size) dec)])
; Your diagonals functions are pretty complex and hard to follow. There's a pretty simple way to
; get the diagonals of any square board - think about the common pattern to the indices of the two
; diagonals ([0 0], [1 1], [2 2], ... and [0, 2], [1, 1], [2, 0], ...). This might be a good thing
; to chat with another apprentice about, to see what they've learned so far.

(defn winning-positions
  []
  (mapv vec (concat (concat (board-rows) (board-columns)) (board-diagonals))))
; `concat` can actually take multiple arguments
; e.g.
; (mapv vec (concat (board-rows) (board-columns) (board-diagonals))))
; also
; `winning-positions`, `board-diagonals`, `board-columns`, `board-rows` all don't actually need to
; be functions, since they don't take any agruments. You could cut things down a bit if you just
; made them all defininitions, rather than functions. They will have to be functions if they ever
; take a variable board size into account though.

(defn move
  [board spot marker]
  (assoc board spot marker))

(defn is-board-full?
  [board]
  (not-any? #(= empty-spot %) board))

(defn is-board-empty?
  [board]
  (every? #(= empty-spot %) board))

(defn is-spot-available?
  [board spot]
  (= empty-spot (board spot)))

(defn available-spots
  [board]
  (map first
    (filter #(= empty-spot (second %))
      (map-indexed vector board))))
; Have you come across clojure's thread-first (->) and thread-last (->>) macros? I don't have enough
; experience with Clojure to tell you when you should use them, but it's good to know that they're
; available to structure your code differently, in a way that may be more readable. For example,
; I re-did your available-spots fn below using the thread-last macro. You can follow the order of
; operations from top to bottom, which is can sometimes be easier to follow. This might be worth
; blogging about if this interests you and you want to look into it further.

; (defn available-spots [board]
;   (->>
;     (map-indexed vector board)
;     (filter #(= empty-spot (second %)))
;     (map first)))

(defn is-valid-move?
  [board spot]
  (and (helpers/in-range? spot board-length)
       (is-spot-available? board spot)))

(defn repeated-markers?
  [board combo]
  (let [selected-combo (for [idx combo] (nth board idx))]
    (if (not-any? #{empty-spot} selected-combo)
      (apply = selected-combo))))
; You can probably simplify this using the `keep-indexed` function.

(defn find-repetition
  [board]
  (filter #(repeated-markers? board %) (winning-positions)))

(defn winning-combo
  [board]
  (first (find-repetition board)))
; I'd just combine the two above functions into
; (defn winning-combo
  ; [board]
  ; (first (filter #(repeated-markers? board %) (winning-positions))))
