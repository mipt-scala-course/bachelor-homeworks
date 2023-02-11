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

libraryDependencies += "org.scalatest"  %% "scalatest"  % "3.2.0" % "test"

wartremoverErrors ++= Seq[Wart](Any, AsInstanceOf, Null, Return, Throw, While, MutableDataStructures)