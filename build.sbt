ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "3.2.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.8.2",
  "co.fs2"            %% "fs2-core"    % "3.7.0"
)

lazy val lang = (project in file("."))
  .settings(name := "bachelor-homeworks")
