import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "distpres"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.apache.pdfbox" % "pdfbox" % "1.7.0",
      "org.apache.poi" % "poi" % "3.8",
      "org.apache.poi" % "poi-scratchpad" % "3.8",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
