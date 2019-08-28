(ns see-el-jay.core
  (:require [clojure.core.async :refer [<!!]]
            [clojure.string :as str]
            [environ.core :refer [env]]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t])
  (:gen-class))


;; (def token (env :telegram-token))
(def token "981703931:AAFUn_wWqiYigy7UZbdr8stOcw_WQt5cyDE")

;; This function get's thread stucked if used with infinite lazy seq's, made a workaround directly inside the telegram handler for run command.
(defn run-code
  "Run given expression and returns a map containing info about the result"
  [strg]
  (let [code-execution (future (try (load-string (subs strg 5)) (catch Exception e (str "Exception: " (.getMessage e)))))
        code-result (deref code-execution 2000 :timeout)
        str-output (pr-str code-result)
        output-size (count str-output)
        result-map {:code-output code-result :str-output str-output :execution-time 0 :issued-code strg}]
    (if (> output-size 200)
      (assoc result-map :str-output "Exception: output ommited. The output is too big.")
      result-map)))

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
                  (let [exec-code (future (:str-output (run-code text)))]
                    (println "Run command issued at " chat)
                    (t/send-text token (:id chat)
                                 {:reply_to_message_id message_id
                                  :parse_mode "HTML"}
                                 (str "<code>" (deref exec-code 2000 :timeout) "</code>")))))
  
  (h/message-fn
   (fn [{{id :id} :chat :as message}]
     (println "Intercepted message: " message)
     (t/send-text token id "I don't do a whole lot ... yet."))))


(defn -main
  [& args]
  (when (str/blank? token)
    (println "Please provde token in TELEGRAM_TOKEN environment variable!")
    (System/exit 1))

  (println "Starting the see-el-jay bot")
  (<!! (p/start token handler)))


;; General ideas
;; /learn                   : Show resources for learning clojure
;; /snippetify -txt OR -img : Save snippet into pastebin/gist or carbon
;; /lib [clojar] [code]     : Fast test a library (that's a hard one)
;; /reset                   : Clear memory and restart application
;; /latest -reditt          : Auto crawl into communities such as clojure Reditt and repost it
;; /events                  : Show upcoming Clojure-related events

