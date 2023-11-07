import _root_.sbt.Keys._

name         := "bachelor-homeworks"
version      := "0.1"
scalaVersion := "3.3.1"

libraryDependencies ++= Seq(
  "io.higherkindness" %% "droste-core" % "0.9.0",
  "io.circe" %% "circe-core" % "0.14.6",
  "org.typelevel" %% "kittens" % "3.1.0"
)

libraryDependencies ++= Seq(
  "org.scalameta" %% "munit" % "0.7.29",
  "io.circe" %% "circe-parser" % "0.14.6"
).map(_ % Test)
