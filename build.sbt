ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "Scala-Challenge"
  )

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= {
  val AkkaVersion = "2.9.3"
  val AkkaHttpVersion = "10.6.3"
  val PostgreSqlJdbc = "42.7.3"
  val AkkaHttpCors = "1.2.0"

  Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "org.postgresql" % "postgresql" % PostgreSqlJdbc,
    "ch.megard" %% "akka-http-cors" % AkkaHttpCors excludeAll ExclusionRule(organization = "com.typesafe.akka")
  )
}
