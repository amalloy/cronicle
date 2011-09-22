(ns cronicle.core
  (:import (java.util.concurrent ScheduledThreadPoolExecutor
                                 ScheduledExecutorService
                                 ScheduledFuture
                                 TimeUnit)))

(let [unit-names {TimeUnit/NANOSECONDS  [:ns :nanosecond :nanoseconds]
                  TimeUnit/MICROSECONDS [:us :microsecond :microseconds]
                  TimeUnit/MILLISECONDS [:ms :millisecond :milliseconds]
                  TimeUnit/SECONDS      [:s :sec :secs :second :seconds]
                  TimeUnit/MINUTES      [:m :min :mins :minute :minutes]
                  TimeUnit/HOURS        [:h :hour :hours]
                  TimeUnit/DAYS         [:d :day :days]}
      unit-lookup (into {} (for [[enum aliases] unit-names
                                 alias aliases]
                             {alias enum}))]
  (defn uglify-time-unit [name]
    (get unit-lookup name name)))

(defn ^ScheduledExecutorService create-threadpool [size]
  (ScheduledThreadPoolExecutor. size))

(let [default-pool-size 6]
  (def ^ScheduledExecutorService default-pool
    (memoize #(create-threadpool default-pool-size))))

(defn ^ScheduledFuture schedule-task
  ([task period unit]
     (schedule-task task (default-pool) period unit))
  ([task pool period unit]
     (.scheduleAtFixedRate pool task 0
                           period (uglify-time-unit unit))))
