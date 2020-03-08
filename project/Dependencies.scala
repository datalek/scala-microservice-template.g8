import sbt._

object Dependencies {
  val commonResolvers = Seq(
    "Typesafe Repo" at "https://repo.typesafe.com/typesafe/releases/"
  )

  object Typesafe {
    val config = "com.typesafe" % "config" % "1.4.0"
  }

  object Akka {
    val http = "com.typesafe.akka" %% "akka-http"   % "10.1.11"
    val stream = "com.typesafe.akka" %% "akka-stream" % "2.5.26" // or whatever the latest version is
    val jsonCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
  }

  object Zio {
    private val version = "1.0.0-RC18-2"
    val core = "dev.zio" %% "zio" % version
    val test = "dev.zio" %% "zio-test" % version
    val testSbt = "dev.zio" %% "zio-test-sbt" % version
    val magnolia = "dev.zio" %% "zio-test-magnolia" % version
  }
}
