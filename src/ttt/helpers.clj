(ns ttt.helpers)

(defn translate-keyword
  [k]
  (if (not (= k :_))
    (str " " (name k) " ")
    "   "))
