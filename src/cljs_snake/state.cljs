(ns cljs-snake.state
  (:require
   [cljs-snake.attributes :refer [attributes]]))

(def state_ (atom {:apple {:x 7 :y 2}
                   :snake `({:x 1 :y 1})
                   :moves `({:x 1 :y 0})}))

(defn is-move-valid? [next-move {[old-move] :moves}]
  (not
   (and
    (= 0 (+ (:x old-move) (:x next-move)))
    (= 0 (+ (:y old-move) (:y next-move))))))

(defn register-move [move]
  (when (is-move-valid? move @state_)
    (swap! state_ update-in [:moves] concat (list move))))

(defn update-apple [{apple :apple}] apple)

(defn update-snake [{snake :snake moves :moves}]
  (let [move (first moves)
        current-head (first snake)
        next-x (+ (:x move) (:x current-head))
        next-y (+ (:y move) (:y current-head))
        new-head {:x next-x
                  :y next-y}]
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
