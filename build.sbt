
ThisBuild / organization := "mipt"
ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "bachelor-homeworks",
    version := "2.8.0",
    libraryDependencies ++=
      "dev.zio" %% "zio" % "2.0.18" ::
      "dev.zio" %% "zio-test" % "2.0.18" ::
      "dev.zio" %% "zio-logging" % "2.1.14" ::
      "org.typelevel" %% "cats-core" % "2.10.0" ::
      "org.typelevel" %% "cats-effect" % "3.5.1" ::  
      Nil
  )
