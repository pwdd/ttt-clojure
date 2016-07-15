(ns ttt.helpers)

(defn clean-string
  [str]
  (clojure.string/lower-case (clojure.string/trim str)))

(defn input-to-number
  [user-input]
  (dec (Integer/parseInt (clean-string user-input))))

(defn in-range?
  [idx limit]
  (and (>= idx 0) (< idx limit)))

(defn stringify-role
  [player-role]
  (if (= :human player-role)
    "human"
    (let [role (name player-role)
         limit (.indexOf role "-")]
      (subs role 0 limit))))

(defn write-game-type
  [first-name second-name]
  (keyword (clojure.string/join "-x-"(sort [first-name second-name]))))
