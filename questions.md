#### Tests

- Do the tests serve as the documentation of the functions or should I be writing the docstrings?

- The test for `helpers/is-int?` takes in consideration the input of strings that cannot be parsed into an integer. However, during game execution, if user enters a letter or spaces before number, the game exits with an exception (`Exception in thread "main" java.lang.NullPointerException`). It also fails if the input has tab key or enter key. How to handle that exception?

- Tests have one `should` per `it` block. In [Speclj tutorial](http://speclj.com/tutorial/step6) there is an example that uses multiples `should`s (`should=`, `should-not` etc) in one block. When to use multiple `should`s?

- `user/get-valid-input`, `game/play` and `main` are not tested.

#### Clojure

- Some articles about Clojure mention things like "the objects inside the vector". Are the elements inside vectors and lists *objects*?

- `for` and `loop / recur` are still not clear...

- Is it better to use `require library :as a-name` or to use a vector with the functions needed in given file?

  ```clojure
  (:require [ttt.board :refer :all]
            [ttt.messenger :as messenger]
            [ttt.game :refer [play]]))
  ```

  That example is from `core.clj`, that uses one function from `game` and two functions from `messenger`. It needs one function from `board`, but also two constants. What would be the better way to deal with it? Is there a way do all the necessary *requirements* in just one file?

- The files were created in a *real-world-like* manner: a board, a user, a game. However, when trying to write some function in `game`, it caused a `cyclic load dependency` between `board` and `game`. So everything is on `board` &mdash; which says a lot about the level of dependency on board.
