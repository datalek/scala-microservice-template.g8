lazy val commonSettings = Seq(
  organization := "com.datalek",
  scalaVersion := "2.12.6",
  crossScalaVersions := Seq("2.11.12", "2.12.6"),
  test in Test := {
    val _ = (g8Test in Test).toTask("").value
  },
  resolvers += Resolver.url("typesafe", url("http://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)
)
val projectName = "scala-microservice-template"
/* the root project, contains startup stuff */
lazy val root = Project(id = s"$projectName", base = file("."))
  .settings(commonSettings: _*)
  .enablePlugins(ScriptedPlugin)
