lazy val commonSettings = Seq(
  organization := "com.rakhov",
  scalaVersion := "2.12.6",
  test in Test := {
    val _ = (g8Test in Test).toTask("").value
  }
)

/* the root project, contains startup stuff */
lazy val root = Project(id = "scala-microservice-template", base = file("."))
  .settings(commonSettings: _*)
  .enablePlugins(ScriptedPlugin)
