(ns ttt.helpers)

(defn clean-string
  [str]
  (clojure.string/lower-case (clojure.string/trim str)))

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

(defn write-game-type
  [first-name second-name]
  (keyword (clojure.string/join "-x-"(sort [first-name second-name]))))
