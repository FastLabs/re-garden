(ns ws.core)


(defn new-sock [url]
  (if-let [chan (js/WebSocket. url)]
    (do
      (set! (.-onmessage chan) (fn [msg] (prn (.-data msg))))
      chan)
    (throw (js/Error. "WebSocket connection failed"))))