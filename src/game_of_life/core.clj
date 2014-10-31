(ns game-of-life.core
  "An implementation of Conway's Game of Life"
  (:require [clojure.string :as string])
  (:import [java.io BufferedReader])
  (:gen-class))

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
  (and (<= 0 x (dec width))
       (<= 0 y (dec height))))

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

(defn- parse-int
  [s]
  (Integer/parseInt s))

(defn- all-cells->alive-set
  [all-cells]
  (reduce (fn [alive row] 
            (apply conj alive (map first (filter second row))))
          (hash-set)
          all-cells))

(defn- parse-all-cells 
  [lines]
  (map-indexed (fn [y line] 
                 (map-indexed (fn [x cell] 
                                [[x y] (= "1" cell)]) 
                              (string/split line #"\s+"))) 
               lines))

(defn read-input
  "Takes a reader and reads input in the form of
  0 1 0 0 0
  1 0 0 1 1
  1 1 0 0 1
  0 1 0 0 0
  1 0 0 0 1
  where 1 is a live cell and 0 is a dead cell. Returns a vector where the 
  first item are the bounds of the cells' world and the second item is the set
  of live cells where each cell is a vector of it's coordinates in the form of
  [x y]."
  [reader]
  (let [all-cells (parse-all-cells (line-seq (BufferedReader. reader)))
        bounds [(count (first all-cells)) (count all-cells)]] 
    [bounds (all-cells->alive-set all-cells)]))

(defn render
  "Given a set of cells and the bounds [width height] of the cells' world 
  prints to the *out* a grid of the live cells."
  [cells [width height]]
  (let [xs (range 0 width)
        ys (range 0 height)]
    (doseq [y ys]
      (doseq [x xs]
        (print (if (contains? cells [x y]) "1" "0"))  
        (print \space))
      (println))
    (println)))

(defn -main
  [& args] 
  (let [[bounds cells] (read-input *in*)]
    (render (next-state cells bounds) bounds)))

