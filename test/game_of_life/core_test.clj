(ns game-of-life.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [game-of-life.core :refer :all]))


(deftest test-blinker
  "Tests to see if a blinker works correctly as illustrated below."
  (testing "Testing a blinker
           0 0 0 0 0
           0 0 0 0 0
           0 1 1 1 0
           0 0 0 0 0
           0 0 0 0 0
           
           0 0 0 0 0
           0 0 1 0 0
           0 0 1 0 0
           0 0 1 0 0
           0 0 0 0 0
           
           0 0 0 0 0
           0 0 0 0 0
           0 1 1 1 0
           0 0 0 0 0
           0 0 0 0 0"
    (let [horizontal #{[1 2] [2 2] [3 2]}
          vertical #{[2 1] [2 2] [2 3]}]
      (is (= vertical (next-state horizontal [6 6])))
      (is (= horizontal (next-state vertical [6 6]))))))

(defn- stable?
  [state bounds]
  (and (= state (next-state state bounds))
       (= state (next-state (next-state state bounds) bounds))))

(deftest test-stable
  "Tests a few cases where the next state is the same as the input state" 
  (testing "square"
    (is (stable? #{[1 1] [2 1] [1 2] [2 2]} [6 6])))
  (testing "oval"
    (is (stable? #{[0 1] [1 0] [1 2] [2 0] [2 2] [3 1]} [6 6]))))

(deftest test-death
  "Tests a few cases that should result in all cells being dead." 
  (testing "lone cell should die"
    (is (empty? (next-state #{[1 1]} [6 6]))))
  (testing "pairs of cells should die"
    (is (empty? (next-state #{[1 1] [1 2]} [6 6])))
    (is (empty? (next-state #{[1 1] [1 2] [4 4] [4 5]} [6 6]))))
  (testing "shape should degenerate to nothing after a few iterations"
    (is (empty? (last (take 5 (iterate #(next-state % [6 6]) 
                                       #{[1 2] [2 2] [3 2] [1 3] [2 3]})))))))

(deftest test-bounds
  "Tests if shapes stay in bounds"
  (testing "test in-bounds? fn"
    (let [bounds [5 5]]
      (is (in-bounds? bounds [3 3]))
      (is (in-bounds? bounds [0 0]))
      (is (in-bounds? bounds [0 4]))
      (is (in-bounds? bounds [4 0]))
      (is (in-bounds? bounds [4 4]))
      (is (not (in-bounds? bounds [-1 0])))
      (is (not (in-bounds? bounds [0 -1])))
      (is (not (in-bounds? bounds [-1 -1])))
      (is (not (in-bounds? bounds [5 0])))
      (is (not (in-bounds? bounds [0 5])))
      (is (not (in-bounds? bounds [5 5])))))
  (testing "blinker on edge"
    (let [bounds [5 5]
          iter1 (next-state #{[0 0] [1 0] [2 0]} bounds)
          iter2 (next-state iter1 bounds)] 
      (is (empty? iter2))
      (every? (partial in-bounds? bounds) iter1))))

(deftest test-read-input
  (testing "reading test-input.txt"
    (let [[bounds cells] (read-input (io/reader "test-input.txt"))]
      (is (= [5 5] bounds))
      (is (= #{[1 0] [0 1] [3 1] [4 1] [0 2] [1 2] [4 2] [1 3] [0 4] [4 4]} 
             cells)))))

#_(run-tests *ns*)



