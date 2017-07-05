name := "Akka Streams"

version := "1.0"

scalaVersion := "2.12.2"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  Resolver.sonatypeRepo("releases")
)

val scalacheck = Seq(
    "org.scalacheck" %% "scalacheck" % "1.13.4"
)

val scalatest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.1"
)

val akkaV = "2.5.3"

val akka = Seq (
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV
)

val logging = Seq (
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.typesafe.akka" %% "akka-slf4j" % akkaV
)

libraryDependencies ++= scalacheck ++ scalatest ++ akka

//initialCommands in console := "ammonite.repl.Main().run()"
