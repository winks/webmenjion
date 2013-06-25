(ns webmenjion.sender
  (:require [clj-http.client :as client]))

(defn get-endpoint
  "Returns the receiver endpoint"
  ([endpoint host]
    (get-endpoint endpoint host "http"))
  ([endpoint host scheme]
    (let [url (str scheme "://" host endpoint)]
      (str "<link href=\"" url "\" rel=\"http://webmention.org/\" />"))))

(defn notify-endpoint
  "Sender Notifies Receiver"
  [endpoint source target]
  (let [response (client/post endpoint
    {:content-type "application/x-www-url-form-encoded"
     :accept :json
     :body (str "source=" source "\n" "target=" target)})]
    (if (= 202 (:status response))
      {:success true :response response}
      {:success false :response response})))
