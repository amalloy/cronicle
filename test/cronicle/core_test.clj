(ns cronicle.core-test
  (:use cronicle.core clojure.test))

(deftest basic-schedule
  (let [a (atom 0)]
    (schedule-task #(swap! a inc) 250 :ms)
    (Thread/sleep 1200)
    (is (= 5 @a)) ; once at startup, then four more times
    ))