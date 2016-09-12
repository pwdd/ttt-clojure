(ns ttt.game.loop
  (:require [ttt.game.game :as game]
            [ttt.messenger :as messenger]
            [ttt.view :as view]
            [ttt.board :as board]
            [ttt.computer.negamax :as negamax]
            [ttt.get-spots :refer [select-spot]]
            [ttt.player :as player]
            [ttt.evaluate-game :as evaluate-game]
            [ttt.prompt :as prompt]
            [ttt.file.writer :as file-writer]
            [ttt.input-validation :as input-validation]
            [ttt.file.reader :as file-reader]
            [ttt.computer.easy :as easy-computer]
            [ttt.computer.medium :as medium-computer]
            [ttt.animated-start :as animate]
            [ttt.helpers :as helpers]))

(def file-extension ".json")
(def first-player-color :bright-green)
(def second-player-color :bright-blue)
(def x {:token :x :color first-player-color})
(def o {:token :o :color second-player-color})
(def animation-starting-board [o o :_ :_ x :_ :_ :_ x])

(defn- setup-regular-game
  [msg-first-attr msg-second-attr]
  (let [board-size (Integer/parseInt (prompt/get-board-size))
        current-player-attr (prompt/get-player-attributes {:msg msg-first-attr
                                                           :color first-player-color})
        opponent-attr
          (prompt/get-player-attributes {:msg msg-second-attr
                                         :color second-player-color
                                         :opponent-marker (:marker current-player-attr)})
        current-player (player/define-player current-player-attr first-player-color :first)
        opponent (player/define-player opponent-attr second-player-color :second)
        game (game/create-game (:role current-player) (:role opponent))]
    {:current-player current-player
     :opponent opponent
     :game game
     :board-size board-size
     :saved false}))

(defn- setup-resumed-game
  [directory]
  (let [files (file-reader/list-all-files directory)
        filename (prompt/choose-a-file files)
        data (file-reader/saved-data (str filename file-extension) directory)
        current-player (data :current-player-data)
        opponent (data :opponent-data)
        game (game/create-game (:role current-player)
                               (:role opponent))
        board (data :board-data)]
    {:current-player current-player
     :opponent opponent
     :game game
     :board board
     :saved true
     :board-size (board/board-size board)}))

(defn game-setup
  [game-selection directory & [msg-first-player-attr msg-second-player-attr]]
  (if (= game-selection input-validation/saved-game-option)
    (setup-resumed-game directory)
    (setup-regular-game msg-first-player-attr msg-second-player-attr)))

(defn- first-view-msgs
  []
  (view/clear-screen)
  (animate/animated-board :computer-x-computer
                          animation-starting-board
                          {:marker x :role :hard-computer}
                          {:marker o :role :easy-computer})
  (view/print-message messenger/welcome))

(defn- initial-view-of-board
  [first-screen saved player board]
  (when (game/human-makes-first-move? first-screen (:role player))
    (if saved
      (view/print-message (messenger/current-player-is (:marker player))))
    (view/print-message (messenger/stringify-board board))))

(defn- display-new-board-info
  [game board current-player spot]
  (view/make-board-disappear (:role current-player) 1000)
  (view/print-message (messenger/moved-to game current-player spot))
  (view/print-message (messenger/stringify-board board)))

(defn- save-and-exit
  [directory filename {:keys [board current-player opponent]}]
  (file-writer/create-game-file directory
                                filename
                                {:board board
                                 :current-player current-player
                                 :opponent opponent})
  (view/print-message messenger/game-saved)
  (Thread/sleep 700)
  (view/clear-and-quit))

(defn restart-data
  [params]
  (let [new-board (board/new-board (:board-size params))]
    (if (player/started-game? (get-in params [:current-player :start-game]))
      (assoc params :board new-board)
      {:board new-board
       :current-player (:opponent params)
       :opponent (:current-player params)
       :board-size (:board-size params)})))

(defn- save-and-exit-data
  [params]
  (select-keys params [:board :current-player :opponent]))

(declare game-loop)

(defn- not-spot-input
  [input game-params]
  (let [directory file-reader/directory]
    (cond
      (input-validation/save? input)
        (save-and-exit directory
                       (prompt/enter-a-file-name (file-reader/list-all-files directory))
                       (save-and-exit-data game-params))
      (input-validation/quit? input) (view/clear-and-quit)
      :else
        (game-loop (restart-data game-params)))))

(defmethod select-spot :human
  [game-params]
  (let [input (prompt/prompt helpers/clean-string messenger/multiple-choice)
        board (:board game-params)]
    (cond
      (or (input-validation/save? input)
          (input-validation/quit? input)
          (input-validation/restart? input))
        (not-spot-input input game-params)
      (input-validation/is-valid-move-input? board input)
        (helpers/input-to-number input)
      :else
      (do
        (view/print-message (messenger/board-after-invalid-input board input))
        (recur game-params)))))

(defn- make-a-move
  [game board current-player opponent saved board-size]
  (select-spot {:game game
                :board-size board-size
                :board board
                :current-player current-player
                :opponent opponent
                :depth negamax/start-depth
                :board-length board/board-length}))

(defn- game-over-msg
  [game board current-player opponent]
  (view/print-message (messenger/result game
                                        board
                                        current-player
                                        opponent)))

(defn- get-game-board
  [board board-size]
  (if (seq board)
    board
    (board/new-board board-size)))

(defn- game-loop
  [{:keys [game board current-player opponent saved first-screen board-size]
    :or {board [] first-screen true}}]

  (let [new-board (get-game-board board board-size)]

    (initial-view-of-board first-screen saved current-player new-board)

    (let [spot (make-a-move game new-board current-player opponent saved board-size)
          game-board (board/move new-board spot (:marker current-player))]

      (display-new-board-info game game-board current-player spot)

      (if (evaluate-game/game-over? game-board)
        (do
          (game-over-msg game game-board current-player opponent)
          (view/print-message view/flush-down)
          (System/exit 0))
        (recur {:game game
                :board game-board
                :opponent current-player
                :current-player opponent
                :saved saved
                :first-screen false
                :board-size board-size})))))

(defn game-selection
  [directory]
  (if (file-reader/is-there-any-file? directory)
    (prompt/get-new-or-saved)
    input-validation/new-game-option))

(defn- setup
  []
  (let [selection (game-selection file-reader/directory)]
    (game-setup selection
                file-reader/directory
                messenger/ask-first-marker-msg
                messenger/ask-second-marker-msg)))

(defn- clean-and-exit
  []
  (println view/flush-down)
  (System/exit 0))

(defn play
  []
  (first-view-msgs)
  (game-loop (setup))
  (clean-and-exit))
