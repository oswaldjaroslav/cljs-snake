(ns cljs-snake.utils
  (:require
   [cljs-snake.attributes :refer [attributes]]))

(defn scale-x [x]
  (.round js/Math (* x (/ (:width attributes) (:col attributes)))))
(defn scale-y [y]
  (.round js/Math (* y (/ (:height attributes) (:row attributes)))))

(defn entries [obj]
  (map #(vector (name %1) %2) (keys obj) (vals obj)))

(defn setup-canvas [container-id attributes]
  (let [canvas (.getElementById js/document (:id attributes))]
    (if canvas
      (.getContext canvas "2d")
      (let [canvas (.createElement js/document "canvas")
            container (.getElementById js/document container-id)]
        (doseq [[name value] (entries attributes)]
          (.setAttribute canvas name value))
        (.appendChild container canvas)
        (.getContext canvas "2d")))))