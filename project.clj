(defproject see-el-jay "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ             "1.1.0"]
                 [morse               "0.2.4"]]

  :plugins [[lein-environ "1.1.0"]
            [lein-heroku "0.5.3"]]

  :main see-el-jay.core
  :aot [see-el-jay.core]
  :target-path "target/%s"
  

  :profiles {:uberjar {:aot :all}}
  :heroku {
           :app-name "vast-atoll-73500"
           :jdk-version "1.8"
           :include-files ["target/uberjar/see-el-jay-0.1.0-SNAPSHOT-standalone.jar"]
           :process-types { "web" "java -jar target/uberjar/see-el-jay-0.1.0-SNAPSHOT-standalone.jar" }})
