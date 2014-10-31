(ns game-of-life.core
  "An implementation of Conway's Game of Life"
  (:require [clojure.string :as string])
  (:gen-class))

(defn render!
  "Given a set of cells and the bounds [width height] of the cells' world 
  prints to the *out* a grid of the live cells."
  [cells [width height]]
  (let [xs (range 0 (inc width))
        ys (range 0 (inc height))]
    (println (string/join " " xs))
    (println (string/join "" (repeat (* 2 (inc width)) \-)))
    (doseq [y ys]
      (doseq [x xs]
        (print (if (contains? cells [x y]) \- \space))  
        (print \space))
      (println "|" y ))))

(defn neighbors
  "Given a set of cells returns a seq of adjacent cells. If n cells share a 
  neighbor then this neighbor will be in the seq n times."
  [cells]
  (for [[x y] cells dx [-1 0 1] dy [-1 0 1]  
        :when (not (and (zero? dx) (zero? dy)))]
    [(+ x dx) (+ y dy)]))

(defn in-bounds?
  "Given bounds [width height] and a cell returns true if the cell is inside
  the box from [0,0] to [width height]."
  [[width height] [x y]]
  (and (<= 0 x width)
       (<= 0 y height)))

(defn next-state
  "Given the current set of live cells and a bounding box [width height] for 
  the cells' world returns the next state as a set of live cells per the rules 
  of Conway's Game of Life."
  [cells bounds]
  (reduce (fn [alive [neighbor freq]]
            (if (and (in-bounds? bounds neighbor)
                     (or (and (cells neighbor) (<= 2 freq 3))
                         (and (not (cells neighbor)) (= 3 freq))))
              (conj alive neighbor)
              alive))
          (hash-set)
          (frequencies (neighbors cells))))

(defn parse-int
  [s]
  (Integer/parseInt s))

(defn -main
  [& [width height iterations & cell-coords]]
  (if (and width height iterations 
           (even? (count cell-coords)))
    (let [bounds [(parse-int width) (parse-int height)]
          iters (parse-int iterations)
          cells (set (partition 2 (map parse-int cell-coords)))
          states (take iters (iterate #(next-state % bounds) cells))]
      (doseq [s states]
        (render! s bounds))
      (System/exit 0))
    (do 
      (println "Arguments: width height iterations x0 y0 x1 y1 x2 y2 ...")
      (System/exit 1))))
