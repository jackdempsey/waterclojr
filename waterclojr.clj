(ns example 
  (:use compojure)) 
; clojure-json located at http://github.com/danlarkin/clojure-json/tree/master
(require '(org.danlarkin [json :as json]))

(def DB (ref #{}))

(defn current-time 
  []
  (/ (. System currentTimeMillis) 1000.0))

(defn gen-id 
  "Generate a random ID. Need to add some Zlib stuff in here"
  []
  (let [base (rand 100000000) 
        salt (current-time)]
    (+ base salt)))

(def greeting (slurp "index.html"))

(defn add-channel
  [id]
  (alter DB conj id))

(defn list-channels 
  []
  (json/encode-to-str @DB))

(defroutes webservice
  (GET "/" 
    greeting)
  (GET "/channels"
    (list-channels))
  (POST "/channels" 
    (let [id (gen-id)]
      (dosync
        (add-channel id))
      (json/encode-to-str id))))

(run-server {:port 8080} 
  "/*" (servlet webservice))
