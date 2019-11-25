import sbt.Keys._
import sbt._
import com.typesafe.sbt.SbtScalariform.autoImport._
import scalariform.formatter.preferences._

object ProjectSettings {

  val common =
    Seq(
      sbt.Keys.publish := {},
      scalariformPreferences := scalariformPreferences.value.setPreference(DanglingCloseParenthesis, Force)
    )

  private val pom = {
    <scm>
      <url>http://git.example/user/repo.git</url>
      <connection>scm:git:git@git.example:user/repo.git</connection>
    </scm>
  }

  private lazy val credentialsPath =
    sys.props.get("artifactory.creds")
      .map(s => new File(s))
      .getOrElse(Path.userHome / ".m2" / ".credentials")

  val publish = Seq(
    publishTo := {
      val nexus = "http://hostname/artifactory/"
      if (isSnapshot.value) Some("snapshots" at nexus + "libs-snapshot-local")
      else Some("releases"  at nexus + "libs-release-local")
    },
    credentials := {
      val default = credentials.value
      if (credentialsPath.exists()) List(Credentials(credentialsPath)) else default
    },
    pomExtra := pom,
    publishArtifact in Test := false
  )
}
