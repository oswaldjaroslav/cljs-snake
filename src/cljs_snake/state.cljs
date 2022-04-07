(ns cljs-snake.state
  (:require
   [cljs-snake.utils :refer [get-random-apple]]
   [cljs-snake.attributes :refer [attributes]]))

(def state_ (atom {:apple {:x 7 :y 2}
                   :snake `({:x 1 :y 1})
                   :moves `({:x 1 :y 0})}))

(defn is-move-valid? [next-move {[old-move] :moves}]
  (not
   (and
    (= 0 (+ (:x old-move) (:x next-move)))
    (= 0 (+ (:y old-move) (:y next-move))))))

(defn is-eating-apple? [head apple]
  (= head apple))

(defn register-move [move]
  (when (is-move-valid? move @state_)
    (swap! state_ update-in [:moves] concat (list move))))

(defn create-future-head [{snake :snake moves :moves}]
  (let [move (first moves)
        current-head (first snake)
        x (+ (:x move) (:x current-head))
        y (+ (:y move) (:y current-head))]
    {:x (cond
          (= x -1) (- (:col attributes) 1)
          (= x (:col attributes)) 0
          :else x)
     :y (cond
          (= y -1) (- (:row attributes) 1)
          (= y (:row attributes)) 0
          :else y)}))

(defn update-apple [future-head {apple :apple}]
  (if (= apple future-head)
    (get-random-apple)
    apple))

(defn update-snake [future-head {snake :snake apple :apple}]
  (->>
   (if (is-eating-apple? future-head apple)
     snake
     (drop-last snake))
   (concat
    (list future-head))))


(defn update-moves [{moves :moves}]
  (if (> (count moves) 1)
    (drop 1 moves)
    moves))

(defn compute-next-state [old-state]
  (let [future-head (create-future-head old-state)]
    {:apple (update-apple future-head old-state)
     :snake (update-snake future-head old-state)
     :moves (update-moves old-state)}))
