ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "ru.unlocker.slurm.spark"
ThisBuild / name := "homework"


val sharedSettings = Seq(
  resolvers ++= Resolver.sonatypeOssRepos("releases"),
  resolvers += ("Artima Maven Repository" at "http://repo.artima.com/releases").withAllowInsecureProtocol(true),
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.0",
    "org.scalatest" %% "scalatest" % "3.0.4" % "test"
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
    "-Xfatal-warnings"
  ),
  scalaVersion := "2.12.18"
)

val circeVersion = "0.14.1"


lazy val root = (project in file("."))
  .aggregate(hw11)
  .settings(name := "homework")

lazy val hw11 = (project in file("hw_01"))
  .settings(
    name := "hw_01",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
  .settings(sharedSettings)

