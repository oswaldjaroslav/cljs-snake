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

(def state_ (atom {:apple {:x 7 :y 2}
                   :snake `({:x 1 :y 1})
                   :moves `({:x 1 :y 0})}))

(def ctx (setup-canvas "app" attributes))

(defn scale-x [x]
  (.round js/Math (* x (/ (:width attributes) (:col attributes)))))
(defn scale-y [y]
  (.round js/Math (* y (/ (:height attributes) (:row attributes)))))

(def test-snake `({:x 1 :y 1}))

(concat [{:x 2 :y 2}] test-snake)


(->>
 test-snake
 (concat [{:x 2 :y 2}])
 (drop-last))

(defn update-apple [{apple :apple}] apple)

(defn update-snake [{snake :snake moves :moves}]
  (let [move (first moves)
        current-head (first snake)
        new-head {:x (+ (:x move) (:x current-head))  
                  :y (+ (:y move) (:y current-head))}]
    (->>
     snake
     (concat (list new-head))
     (drop-last))))


(defn update-moves [{moves :moves}]
  (if (> (count moves) 1)
    (drop 1 moves)
    moves))

(defn compute-next-state [old-state]
  {:apple (update-apple old-state)
   :snake (update-snake old-state)
   :moves (update-moves old-state)})

(defn register-move [move]
  (swap! state_ update-in [:moves] concat (list move)))


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
    (if (> (- t2 t1) 100)
      (do
        (swap! state_ compute-next-state)
        (draw)
        (.requestAnimationFrame js/window (step t2)))
      (.requestAnimationFrame js/window (step t1)))))

(def directions {:NORTH {:x 0 :y -1}
                 :SOUTH {:x 0 :y 1}
                 :EAST {:x 1 :y 0}
                 :WEST {:x -1 :y 0}})

(.addEventListener
 js/window
 "keydown"
 (fn [event]
   (let [keyCode (.-keyCode event)]
     (cond
       (= keyCode 38) (register-move (:NORTH directions))
       (= keyCode 40) (register-move (:SOUTH directions))
       (= keyCode 39) (register-move (:EAST directions))
       (= keyCode 37) (register-move (:WEST directions))))))


(defn run-game []
  (println "Running the game")
  ((step 0) 0))
