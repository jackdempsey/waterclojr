(ns example 
  (:use compojure)) 

(defn current-time (/ (. System currentTimeMillis) 1000.0))

(defn gen-id
  (let [base (.toString (rand 100000000))
        salt (.toString current-time)]
    ; use zlib to crc32 this sheeit
    ; Zlib.crc32(base + salt).to_s(36)
    ))

(def greeting (slurp "index.html"))

(defroutes webservice
  (GET "/" 
    greeting) 
  (POST "/channels" 
    (let [id gen-id]
      (dosync
        (add-channel {:name id})
        (to-json {:id id})))))

(run-server {:port 8080} 
  "/*" (servlet webservice))
