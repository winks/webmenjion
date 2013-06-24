(ns webmenjion.receiver
  (:require [clj-http.client :as client]
            [clojure.contrib.string :as s]
            [clojure.data.json :as json]))

(def errors {:source_not_found "The source URI does not exist."})

(defn verify-self
  "Verification that target is a valid url on the receiver side."
  [target func]
   (func target))

(defn verify-remote
  "Verification that source contains target"
  [source target]
  (let [response (client/get source)]
    (s/substring? target (:body response))))

(defn respond-success
  "Response - Success, json or html"
  ([content-type]
    (respond-success content-type "WebMention was successful"))
  ([content-type msg]
    (if (= "json" content-type)
      {:status 202
       :headers {"Content-Type" "application/json"}
       :body (json/write-str {:result msg})}
      {:status 202
       :headers {"Content-Type" "text/html"}
       :body (str "<!DOCTYPE html>" "\n" "<html><head><title>WebMention</title></head>"
             "<body><p>" msg "</p></body></html>")})))

(defn respond-error
  "Response - Error"
  [content-type error description]
  (if (= "json" content-type)
    {:status 400
     :headers {"Content-Type" "application/json"}
     :body (json/write-str {:error error :error_description description})}
    {:status 400
     :headers {"Content-Type" "text/html"}
     :body (str "<!DOCTYPE html>" "\n" "<html><head><title>WebMention Error</title></head>"
             "<body><h2>" error "</h2>" "\n" "<p>" description "</p></body></html>")}))
