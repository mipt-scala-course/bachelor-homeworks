import _root_.sbt.Keys._
import wartremover.Wart
import wartremover.Wart._

name := "bachelor-homeworks"
version := "0.1"
scalaVersion := "2.13.10"

scalacOptions := List(
  "-encoding",
  "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-Ymacro-annotations"
)

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest"               % "3.2.10"  % Test,
    "org.scalamock" %% "scalamock"               % "5.1.0"   % Test,
    "org.mockito"   %% "mockito-scala"           % "1.16.49" % Test,
    "org.mockito"   %% "mockito-scala-scalatest" % "1.16.49" % Test
)


wartremoverErrors ++= Seq[Wart](Any, AsInstanceOf, Null, Return, Throw, While, MutableDataStructures)