(ns webmenjion.sender-test
  (:use clojure.test
        webmenjion.sender))

(deftest get-endpoint-test-http
  (testing
    (is (= "<link href=\"http://example.org/foo\" rel=\"http://webmention.org/\" />"
      (get-endpoint "/foo" "example.org")))))

(deftest get-endpoint-test-https
  (testing
    (is (= "<link href=\"https://example.org/foo\" rel=\"http://webmention.org/\" />"
      (get-endpoint "/foo" "example.org" "https")))))
