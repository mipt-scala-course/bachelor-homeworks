ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "3.2.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.8.2",
  "co.fs2"            %% "fs2-core"    % "3.7.0",
  "org.typelevel"     %% "cats-effect" % "3.5.2",
  "org.typelevel"     %% "cats-mtl"    % "1.3.1",
  "org.scalatest"     %% "scalatest"   % "3.2.15" % "test"
)

scalacOptions := List("-Ykind-projector")

lazy val lang = (project in file("."))
  .settings(name := "bachelor-homeworks")
