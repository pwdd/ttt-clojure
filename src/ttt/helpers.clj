(ns ttt.helpers)

(defn spot-to-string [number]
  (str " " (+ 1 number) " "))

(defn keyword-to-string [key]
  (str " " (name key) " ")
)

(defn to-string [element]
  (if (integer? element)
    (spot-to-string element)
    (keyword-to-string element)
  )
)
