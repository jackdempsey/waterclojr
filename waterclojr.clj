(ns example 
  (:use compojure)) 

(def DB (ref #{}))

(defn current-time 
  []
  (/ (. System currentTimeMillis) 1000.0))

(defn gen-id 
  []
  (let [base (rand 100000000) salt current-time]
    (+ base salt)))

(def greeting (slurp "index.html"))

(defn add-channel
  [id]
  (alter DB conj id))

(defn list-channels 
  [& args]
  (@DB))

(defroutes webservice
  (GET "/" 
    greeting) 
  (GET "/channels"
    list-channels)
  (POST "/channels" 
    (let [id gen-id]
      (dosync
        (add-channel id)))))

(run-server {:port 8080} 
  "/*" (servlet webservice))
