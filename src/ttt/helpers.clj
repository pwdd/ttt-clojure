(ns ttt.helpers)

(defn clean-string
  [str]
  (clojure.string/lower-case (clojure.string/trim str)))

(defn translate-keyword
  [k]
  (if (not (= k :_))
    (str " " (name k) " ")
    "   "))

(defn is-int?
  [user-input]
  (try
    (Integer/parseInt (clean-string user-input))
    true
  (catch Exception e false
    )))

(defn input-to-number
  [user-input]
  (dec (Integer/parseInt (clean-string user-input))))

(defn in-range?
  [idx limit]
  (and (>= idx 0) (< idx limit)))
