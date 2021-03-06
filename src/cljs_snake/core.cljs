(ns cljs-snake.core
  (:require
   [cljs-snake.snake :refer [run-game]]
   [cljs-snake.config :as config]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

;; (defn ^:dev/after-load mount-root []
;;   (let [root-el (.getElementById js/document "app")]
;;     (rdom/unmount-component-at-node root-el)
;;     (rdom/render [views/main-panel] root-el)))

(defn init []
  (dev-setup)
  (run-game))
