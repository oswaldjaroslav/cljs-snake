(ns cljs-snake.utils)


(defn entries [obj]
  (map #(vector (name %1) %2) (keys obj) (vals obj)))

(defn setup-canvas [container-id attributes]
  (let [canvas (.createElement js/document "canvas")
        container (.getElementById js/document container-id)]
    (doseq [[name value] (entries attributes)]
      (.setAttribute canvas name value))
    (.appendChild container canvas)
    (.getContext canvas "2d")))