(ns webmenjion.receiver
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(def errors {:source_not_found "The source URI does not exist."
             :target_not_found "The target URI does not exist."
             :target_not_supported "The specified target URI is not a WebMention-enabled resource."
             :no_link_found "The source URI does not contain a link to the target URI."
             :already_registered "The specified WebMention has already been registered."})

(defn verify-self
  "Verification that target is a valid url on the receiver side."
  [target func]
   (func target))

(defn verify-remote
  "Verification that source contains target"
  [source target]
  (let [response (client/get source)]
    (.contains (:body response) target)))

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
  ([content-type error]
    (respond-error content-type error (get errors error)))
  ([content-type error description]
    (if (= "json" content-type)
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (json/write-str {:error (name error) :error_description description})}
      {:status 400
       :headers {"Content-Type" "text/html"}
       :body (str "<!DOCTYPE html>" "\n" "<html><head><title>WebMention Error</title></head>"
               "<body><h2>" (name error) "</h2>" "\n" "<p>" description "</p></body></html>")})))
