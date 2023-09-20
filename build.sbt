ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.1"

val munit = "0.7.29"

lazy val root = (project in file("."))
  .settings(
    name := "bachelor-homeworks",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % munit % Test
    )
  )
