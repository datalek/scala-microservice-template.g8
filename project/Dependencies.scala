import sbt._

object Dependencies {
  val commonResolvers = Seq(
    "Typesafe Repo" at "https://repo.typesafe.com/typesafe/releases/"
  )

  object Logback {
    val classic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  }

  object Typesafe {
    val config = "com.typesafe" % "config" % "1.4.0"
  }

  object Akka {
    val http = "com.typesafe.akka" %% "akka-http"   % "10.1.11"
    val stream = "com.typesafe.akka" %% "akka-stream" % "2.5.26" // or whatever the latest version is
    val jsonCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
  }

  object Circe {
    val version = "0.13.0"
    val generic = "io.circe" %% "circe-generic" % version
  }

  object Zio {
    private val version = "1.0.0-RC21-2"
    val core = "dev.zio" %% "zio" % version
    val test = "dev.zio" %% "zio-test" % version
    val testSbt = "dev.zio" %% "zio-test-sbt" % version
    val magnolia = "dev.zio" %% "zio-test-magnolia" % version
  }

  object Cats {
    private val version = "2.1.3"
    val effect = "org.typelevel" %% "cats-effect" % version
  }

  object Http4s {
    private val version = "0.21.6"
    val dsl = "org.http4s" %% "http4s-dsl" % version
    val server = "org.http4s" %% "http4s-blaze-server" % version
    val client = "org.http4s" %% "http4s-blaze-client" % version
    val circe = "org.http4s" %% "http4s-circe" % version
  }
}
