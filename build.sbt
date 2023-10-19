
ThisBuild / organization := "mipt"
ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "bachelor-homeworks",
    version := "1.0.0",
    libraryDependencies ++=
      "dev.zio" %% "zio" % "2.0.18" ::
      "org.typelevel" %% "cats-core" % "2.10.0" ::
      "org.typelevel" %% "cats-effect" % "3.5.1" ::  
      Nil
  )
