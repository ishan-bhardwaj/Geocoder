name := "Geocoder"

version := "1.0"

scalaVersion := "2.11.11"

val akkaVersion = "2.5.11"
val akkaHttpVersion = "10.1.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.apache.solr" % "solr-solrj" % "7.3.0"
)


