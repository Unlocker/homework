ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "ru.unlocker.slurm.spark"
ThisBuild / name := "homework"

val sharedSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.2.17",
    "org.scalatest" %% "scalatest" % "3.2.17" % "test"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "utf8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ymacro-annotations"
  )
)


lazy val root = (project in file("."))
  .aggregate(hw11)
  .settings(name := "homework")

lazy val hw11 = (project in file("hw_01"))
  .settings(sharedSettings)
