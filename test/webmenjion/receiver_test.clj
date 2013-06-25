(ns webmenjion.receiver-test
  (:use clojure.test
        webmenjion.receiver))

(deftest respond-success-test-1
  (testing
    (is (= {:status 202
            :headers {"Content-Type" "application/json"}
            :body "{\"result\":\"WebMention was successful\"}"}
      (respond-success "json")))))

(deftest respond-success-test-2
  (testing
    (is (= {:status 202
            :headers {"Content-Type" "application/json"}
            :body "{\"result\":\"foo\"}"}
      (respond-success "json" "foo")))))

(deftest respond-success-test-3
  (testing
    (is (= {:status 202
            :headers {"Content-Type" "text/html"}
            :body (str "<!DOCTYPE html>\n<html><head><title>WebMention</title></head>"
                  "<body><p>foo</p></body></html>")}
      (respond-success "json2" "foo")))))

(deftest respond-error-test-1
  (testing
    (is (= {:status 400
            :headers {"Content-Type" "application/json"}
            :body "{\"error\":\"source_not_found\",\"error_description\":\"The source URI does not exist.\"}"}
      (respond-error "json" :source_not_found)))))

(deftest respond-error-test-2
  (testing
    (is (= {:status 400
            :headers {"Content-Type" "text/html"}
            :body (str "<!DOCTYPE html>\n<html><head><title>WebMention Error</title></head>"
                  "<body><h2>source_not_found</h2>\n<p>The source URI does not exist.</p></body></html>")}
      (respond-error "json2" :source_not_found "The source URI does not exist.")))))

(defn tmp
  [x]
  (= "foo" x))

(deftest verify-self-test
  (testing
    (is (true? (verify-self "foo" tmp)))))
