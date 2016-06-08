(ns ttt.messenger-spec
  (:require [speclj.core :refer :all]
            [ttt.messenger :refer [prompt]]))

(describe "messenger"
  
  (it "outputs messages to stdout"
    (should= (with-out-str (prompt "anything")) "anything\r\n")
  )
)
