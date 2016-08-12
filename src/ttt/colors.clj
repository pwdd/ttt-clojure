(ns ttt.colors)

(def escape (char 27))

(defn color-code
  [color]
  (str escape color))

(def ansi-colors
  {:default (color-code "[37m")
   :blue (color-code "[34m")
   :red (color-code "[31m")
   :yellow (color-code "[33m")
   :green (color-code "[36m")
   :gray (color-code "[37m")
   :reset (color-code "[m")})
