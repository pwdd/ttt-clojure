(ns ttt.view)

(def half-screen 60)
(def screen-lines 5)

; TODO test
(defn clear-screen
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H"))
  (print (clojure.string/join (repeat screen-lines "\n"))))

(defn number-of-spaces
  [message-length]
  (let [half-message (int (Math/ceil (/ message-length 2.0)))]
    (- half-screen half-message)))

(defn padding-spaces
  [message]
  (clojure.string/join
    (repeat (number-of-spaces (count message)) " ")))

(defn add-padding-spaces
  [message]
  (str "\n"
       (clojure.string/join "\n" (map
                                    #(str (padding-spaces %) %)
                                    (clojure.string/split-lines message)))))

(defn print-message
  [msg]
  (println (add-padding-spaces msg)))

; TODO test
(defn centralize-cursor
  []
  (do (print (padding-spaces "")) (flush)))

; TODO test
(defn make-board-disappear
  [player-role]
  (if (or (= :easy-computer player-role)
          (= :hard-computer player-role))
    (do (Thread/sleep 1000)
        (clear-screen))))
