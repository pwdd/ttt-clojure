(ns ttt.game)

(defrecord Game [player-roles])

(defn same-roles?
  [first-player-role second-player-role]
  (= first-player-role second-player-role))

(defn computer-x-human?
  [first-player-role second-player-role]
  (and (some #{:human} [first-player-role second-player-role])
       (not (every? #{:human} [first-player-role second-player-role]))))

(defn game-players-roles
  [first-player-role second-player-role]
  (cond
    (same-roles? first-player-role second-player-role) :same-roles
    (computer-x-human? first-player-role second-player-role) :computer-x-human
    :else
      :computer-x-computer))

(defn create-game
  [first-player-role second-player-role]
  (->Game (game-players-roles first-player-role second-player-role)))

(defn human-makes-first-move?
  [first-screen player-role]
  (and first-screen (= :human player-role)))
