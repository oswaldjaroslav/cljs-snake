(ns cljs-snake.snake
  (:require
   [cljs-snake.utils :refer [setup-canvas]]))

(def attributes {:width 600
                 :height 600
                 :col 20
                 :row 20
                 :id "snake-canvas"})

(def theme {:bg-color "#232323"
            :snake-color "green"
            :apple-color "red"})

(def directions {:NORTH {:x 0 :y -1}
                 :SOUTH {:x 0 :y 1}
                 :EAST {:x 1 :y 0}
                 :WEST {:x -1 :y 0}})

(def state_ (atom {:apple {:x 7 :y 2}
                   :snake [{:x 1 :y 1}]}))

(def ctx (setup-canvas "app" attributes))

(defn scale-x [x]
  (.round js/Math (* x (/ (:width attributes) (:col attributes) ))))
(defn scale-y [y]
  (.round js/Math (* y (/ (:height attributes) (:row attributes)))))

(defn compute-next-state [old-state] old-state)

(defn draw []
  (let [state @state_
        apple (:apple state)
        snake (:snake state)]
    ;; clear canvas
    (set! (.-fillStyle ctx) (:bg-color theme))
    (.fillRect ctx 0 0 (:width attributes) (:height attributes))

  ;; draw snake
    (set! (.-fillStyle ctx) (:snake-color theme))
    (doseq [snake-item snake]
      (.fillRect ctx
                 (scale-x (:x snake-item))
                 (scale-y (:y snake-item))
                 (scale-x 1)
                 (scale-y 1)))

  ;; draw apple
    (set! (.-fillStyle ctx) (:apple-color theme))
    (println apple)
    (.fillRect ctx
               (scale-x (:x apple))
               (scale-y (:y apple))
               (scale-x 1)
               (scale-y 1))))

(defn step [t1]
  (fn [t2]
    (if (> (- t2 t1) 1000)
      (do
        (swap! state_ compute-next-state)
        (draw)
        (.requestAnimationFrame js/window (step t2)))
      (.requestAnimationFrame js/window (step t1)))))

(defn run-game []
  (println "Running the game")
  ((step 0) 0))
