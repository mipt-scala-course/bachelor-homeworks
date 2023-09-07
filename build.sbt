ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.1"

val circe = "0.14.5"
val munit = "0.7.29"

lazy val root = (project in file("."))
  .settings(
    name := "bachelor-homeworks",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circe,
      "org.scalameta" %% "munit" % munit % Test
    )
  )
