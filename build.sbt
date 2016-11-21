name := "simple-sbt"

organization := "com.example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "org.apache.tika" % "tika-core" % "1.13",
  "org.apache.tika" % "tika-parsers" % "1.13"
)

