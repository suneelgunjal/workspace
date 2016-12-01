(ns web.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes getTime
  (	
	GET "/time/:timezone" [timezone] 
	
	;;Assume input is separated by -, form the correct input 
	(def input (clojure.string/replace timezone #"-" "/"))
	
	;;Format the current date and time based on the time zone
	(def dateFormat (java.text.SimpleDateFormat. "dd-MMM-yyyy HH:mm:ss"))
	(def newTimeZone (. java.util.TimeZone getTimeZone input))
	(def var (. dateFormat setTimeZone newTimeZone))
	(def date (java.util.Date.))
	(def output (. dateFormat format date))
	(def id (. newTimeZone getID))
	
	;;If time zone is not understood by API then it returns the default time zone in GMT, if time zone is valid then API returns the same string id that is provided as input e.g Europe/Berlin
	(if (= (compare id "GMT") 0)
		(str "<h1 style='color:red;'>Invalid time zone: " timezone "<h1>")
		(str "<h1>Date and time in " (clojure.string/upper-case input) " time zone: " output"</h1>")
	)
  )
  (route/not-found "Not Found")
  )

(def app
  (wrap-defaults getTime site-defaults))
