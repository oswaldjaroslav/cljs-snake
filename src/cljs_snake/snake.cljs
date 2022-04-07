(ns cljs-snake.snake
  (:require
   [cljs-snake.attributes :refer [attributes]]
   [cljs-snake.state :refer [state_ register-move compute-next-state]]
   [cljs-snake.utils :refer [setup-canvas]]))


(def theme {:bg-color "#232323"
            :snake-color "green"
            :apple-color "red"})


(def ctx (setup-canvas "app" attributes))

(defn scale-x [x]
  (.round js/Math (* x (/ (:width attributes) (:col attributes)))))
(defn scale-y [y]
  (.round js/Math (* y (/ (:height attributes) (:row attributes)))))


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


(.addEventListener
 js/window
 "keydown"
 (fn [event]
   (let [keyCode (.-keyCode event)]
     (cond
       ;; UP
       (= keyCode 38) (register-move {:x 0 :y -1})
       ;; DOWN
       (= keyCode 40) (register-move {:x 0 :y 1})
       ;; RIGHT
       (= keyCode 39) (register-move {:x 1 :y 0})
       ;; LEFT
       (= keyCode 37) (register-move {:x -1 :y 0})))))


(defn run-game []
  (println "Running the game")
  ((step 0) 0))
