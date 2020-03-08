import Dependencies._

lazy val commonSettings = Seq(
  organization := "io.rakhov",
  scalaVersion := "2.12.6",
  crossScalaVersions := Seq("2.11.12", "2.12.6"),
  coverageEnabled in Test := true,
  resolvers ++= Dependencies.commonResolvers,
  exportJars := true
) ++ ProjectSettings.common

lazy val projectName = "exampleservice"

/* the root project, it is a simple aggregator of other modules */
lazy val root = Project(id = s"$projectName", base = file("."))
  .settings(commonSettings: _*)
  .settings(mainClass in Compile := (mainClass in Compile in main).value)
  .settings(addCommandAlias("run", s"${main.id}/run"))
  .aggregate(main, http)

lazy val main = Project(id = s"$projectName-main", base = file(s"$projectName-main"))
  .settings(commonSettings: _*)
  .settings(executableScriptName := "startup")
  .enablePlugins(JavaAppPackaging)
  .settings(libraryDependencies ++= Seq(
    Typesafe.config, Cats.effect, Logback.classic
  ))
  .dependsOn(http)

lazy val http = Project(id = s"$projectName-http", base = file(s"$projectName-http"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    Http4s.circe, Http4s.dsl, Http4s.server,
    Cats.effect, Circe.generic
  ))