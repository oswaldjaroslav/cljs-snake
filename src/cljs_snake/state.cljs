(ns cljs-snake.state
  (:require
   [cljs-snake.attributes :refer [attributes]]))

(defn get-initial-state []
  {:apple {:x 7 :y 2}
   :snake `({:x 1 :y 1})
   :velocity {:x 1 :y 0}
   :game-over false})

(def state_ (atom (get-initial-state)))

(some  #(= % {:x 0 :y 0}) (:snake @state_))

(defn is-move-valid? [next-move {velocity :velocity}]
  (not
   (and
    (= 0 (+ (:x velocity) (:x next-move)))
    (= 0 (+ (:y velocity) (:y next-move))))))

(defn is-coliding-with-snake [position snake]
  (some  #(= % position) snake))

(defn get-random-apple [snake]
  (let [apple {:x (.floor js/Math (rand (:col attributes)))
               :y (.floor js/Math (rand (:row attributes)))}]
    (if (is-coliding-with-snake apple snake)
      (get-random-apple snake)
      apple)))

(defn is-eating-apple? [head apple]
  (= head apple))

(defn register-move [move]
  (when (is-move-valid? move @state_)
    (swap! state_ #(merge % {:velocity move}))))

(defn create-future-head [{:keys [snake velocity]}]
  (let [current-head (first snake)
        x (+ (:x velocity) (:x current-head))
        y (+ (:y velocity) (:y current-head))]
    {:x (cond
          (= x -1) (- (:col attributes) 1)
          (= x (:col attributes)) 0
          :else x)
     :y (cond
          (= y -1) (- (:row attributes) 1)
          (= y (:row attributes)) 0
          :else y)}))

(defn update-apple [future-head {apple :apple snake :snake}]
  (if (= apple future-head)
    (get-random-apple snake)
    apple))

(defn update-snake [future-head {snake :snake apple :apple}]
  (->>
   (if (is-eating-apple? future-head apple)
     snake
     (drop-last snake))
   (concat
    (list future-head))))


(defn update-velocity [{velocity :velocity}] velocity)

(defn compute-next-state [old-state]
  (let [future-head (create-future-head old-state)]
    (if (is-coliding-with-snake future-head (:snake old-state))
      (update-in old-state [:game-over] (fn [] true))
      {:apple (update-apple future-head old-state)
       :snake (update-snake future-head old-state)
       :velocity (update-velocity old-state)})))
