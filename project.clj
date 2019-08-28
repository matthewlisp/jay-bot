(defproject see-el-jay "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ             "1.1.0"]
                 [morse               "0.2.4"]]

  :plugins [[lein-environ "1.1.0"]]

  :main see-el-jay.core
  :target-path "target/%s"
  :min-lein-version "2.0.0"
  :uberjar-name "jaybot.jar"
  

  :profiles {:uberjar {:aot :all}})
