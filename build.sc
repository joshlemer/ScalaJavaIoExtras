import mill._, scalalib._, publish._

object java_io_extras extends ScalaModule with PublishModule {
  def scalaVersion = "2.13.0-M4"
  def publishVersion = "0.0.1"

  def pomSettings = PomSettings(
    description = "Extra integrations between Scala and java.io",
    organization = "com.github.joshlemer",
    url = "https://github.com/joshlemer/ScalaJavaIoExtras",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("joshlemer", "ScalaJavaIoExtras"),
    developers = Seq(
      Developer("joshlemer", "Josh Lemer","https://github.com/joshlemer")
    )
  )
}

/*
name := "InputStreamIterator"
version := "0.1"
scalaVersion := "2.13.0-M4"
libraryDependencies +=  "com.novocode" % "junit-interface" % "0.11" % Test

publishTo := Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/content/repositories/snapshots")


 */


