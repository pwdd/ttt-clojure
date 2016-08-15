(ns ttt.colors)

(defn color-code
  [code-string]
  (let [escape (char 27)]
    (str escape code-string)))

(def colors-code-list
  (mapv #(color-code %) ["[31m" "[32m" "[33m" "[34m" "[35m" "[36m" "[37m"]))

(def colors-key-list [:red :green :yellow :blue :purple :cyan :default])

(def ansi-colors
  (zipmap colors-key-list colors-code-list))

(defn colorize
  [color string]
  (str (color ansi-colors) string (:default ansi-colors)))
