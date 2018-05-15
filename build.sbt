name := "todo-list2"

version := "0.1"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.9"
val akkaHttpVersion = "10.1.0-RC2"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
libraryDependencies += "com.h2database" % "h2" % "1.3.148"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"
