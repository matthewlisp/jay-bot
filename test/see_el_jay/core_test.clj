(ns see-el-jay.core-test
  (:require [clojure.test :refer :all]
            [see-el-jay.core :refer :all]))

(def error-msg-terminated "Exception: execution terminated. Your code got too long to execute.")
(def error-msg-output "Exception: output ommited. The output is too big.")
(def error-msg-exception "\"Exception: clojure.lang.ArityException: Wrong number of args (0) passed to: core/repeat, compiling:(null:1:1)\"")

(deftest test-run-code
  (testing "Regular code"
    (is (= 2 (:code-output (run-code "/run (+ 1 1)"))))
    (is (= [2,3,4] (:code-output (run-code "/run (map inc [1 2 3])"))))
    (is (= true (:code-output (run-code "/run (= 1 1)")))))
  (testing "Output is too big"
    (is (= error-msg-output (:str-output (run-code "/run (take 1000 (repeat \"Hi\"))")))))
  (testing "recover from exceptions"
    (is (= error-msg-exception (:str-output (run-code "/run (take 5 (repeat ))"))))))

;; Infinite loops and infinite lazy seqs aren't tested in run-code fn because of a workaround made in morse handler for run command
