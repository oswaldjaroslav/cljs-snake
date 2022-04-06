(ns cljs-snake.snake
  (:require
   [cljs-snake.utils :refer [setup-canvas]]))

(def attributes {:width 600
                 :height 600
                 :col 20
                 :row 20
                 :id "snake-canvas"})

(def state_ (atom {}))

(def ctx (setup-canvas "app" attributes))

(defn compute-next-state [old-state])

(defn step [t1]
  (fn [t2]
    (if (> (- t2 t1) 1000)
      (do
        (println "drawing")
        (.requestAnimationFrame js/window (step t2)))
      (.requestAnimationFrame js/window (step t1)))))



(defn run-game []
  (println "Running the game")
  ((step 0) 0))
