(ns ttt.helpers)

(defn translate-keyword
  [k]
  (if (not (= k :_))
    (str " " (name k) " ")
    "   "))

(defn is-int?
  [user-input]
  (try
    (Integer/parseInt (clojure.string/trim user-input))
    true
  (catch Exception e false
    )))

(defn input-to-number
  [user-input]
  (- (Integer/parseInt user-input) 1))
