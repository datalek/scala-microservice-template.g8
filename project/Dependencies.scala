import sbt._

object Dependencies {
  val commonResolvers = Seq(
    "Typesafe Repo" at "https://repo.typesafe.com/typesafe/releases/"
  )

  object Typesafe {
    val config = "com.typesafe" % "config" % "1.4.0"
  }

  object Zio {
    val core = "dev.zio" %% "zio" % "1.0.0-RC17"
  }
}
