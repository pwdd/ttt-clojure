(ns ttt.colors)

(defn color-code
  [code-string]
  (let [escape (char 27)]
    (str escape code-string)))

(def colors-code-list
  (mapv color-code ["[31m" 
                    "[1;31m" 
                    "[32m" 
                    "[1;32m" 
                    "[33m"
                    "[1;33m"
                    "[34m"
                    "[1;34m" 
                    "[35m"
                    "[1;35m"
                    "[36m"
                    "[1;36m" 
                    "[0;37m" 
                    "[1;30m"]))

(def colors-key-list [:red 
                      :bright-red 
                      :green 
                      :bright-green 
                      :yellow 
                      :bright-yellow
                      :blue 
                      :bright-blue
                      :purple 
                      :bright-purple
                      :cyan 
                      :bright-cyan
                      :default 
                      :gray])

(def ansi-colors
  (zipmap colors-key-list colors-code-list))

(defn colorize
  [color string]
  (str (color ansi-colors) string (:default ansi-colors)))
