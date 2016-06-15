(ns ttt.core
  ; FIXME temporarily load all so there is access on repl
  (:require [ttt.board :refer :all]
            [ttt.messenger :as messenger]
            [ttt.game :refer [play]]))

(defn -main
  []
  (println messenger/instructions)
  (println messenger/board-representation)
  (play (new-board) first-player second-player)
  )
