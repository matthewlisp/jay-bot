(ns see-el-jay.core
  (:require [clojure.core.async :refer [<!!]]
            [clojure.string :as str]
            [environ.core :refer [env]]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t]))




(defn run-code
  "Run given expression and returns a string as a result"
  [strg]
  (let [code-output (try (eval (read-string (subs strg 5))) (catch Exception e (str "Exception: " (.getMessage e))))
        str-output (pr-str code-output)
        output-size (count str-output)]
    (if (> output-size 3000)
      "Exception: output ommited. The output is too big."
      str-output)))

(def token (env :telegram-token))

(h/defhandler handler

  (h/command-fn "start"
                (fn [{{id :id :as chat} :chat}]
                  (println "Bot joined new chat: " chat)
                  (t/send-text token id "Welcome to see-el-jay!")))

  (h/command-fn "help"
                (fn [{{id :id :as chat} :chat}]
                  (println "Help was requested in " chat)
                  (t/send-text token id "Help is on the way")))

  (h/command-fn "run"
                (fn [{:keys [text chat message_id]}]
                  (println "Run command issued at " chat)
                  (t/send-text token (:id chat)
                               {:reply_to_message_id message_id
                                :parse_mode "HTML"}
                               (str "<code>" (run-code text) "</code>"))))
  
  (h/message-fn
   (fn [{{id :id} :chat :as message}]
     (println "Intercepted message: " message)
     (t/send-text token id "I don't do a whole lot ... yet."))))


(defn -main
  [& args]
  (when (str/blank? token)
    (println "Please provde token in TELEGRAM_TOKEN environment variable!")
    (System/exit 1))

  (println "Starting the see-el-jay")
  (<!! (p/start token handler)))


;; General ideas:
;; /learn - Show resources for learning clojure
;; /snippetify -txt -img - Save snippet into pastebin/gist or carbon
;; /lib [clojar] code - Fast test a library (that's a hard one)
;; /reset - Clear memory and restart application
;; Auto crawl into communities such as clojure Reditt and repost it
