(ns see-el-jay.core-test
  (:require [clojure.test :refer :all]
            [see-el-jay.core :refer :all]))

(def error-msg-terminated "Exception: execution terminated. Your code got too long to execute.")
(def error-msg-output "Exception: output ommited. The output is too big.")
(def error-msg-symstore "Exception: forbidden code. You can't store symbols, use local scope only.")

(deftest test-run-code
  (testing "Regular code"
    (is (= 2 (run-code "/run (+ 1 1)")))
    (is (= [2,3,4] (run-code "/run (map inc [1 2 3])")))
    (is (= true (run-code "/run (= 1 1)"))))
  (testing "Output is too big"
    (is (= error-msg-output (run-code "/run (take 1000 (repeat \"Hi\"))"))))
  (testing "Infinite loop or code that get's too long to run"
    (is (= error-msg-terminated (run-code "/run (repeat \"Hi\")"))))
  (testing "Snippet too big to be processed") ;; DO LATER
  (testing "recover from exceptions"
    (is (= "Exception: Wrong number of args (0) passed to: core/repeat" (run-code "/run (take 5 (repeat ))")))))
