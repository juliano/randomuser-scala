import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile

inThisBuild(List(
    organization := "io.github.juliano",
    version := "0.1.4",
    homepage := Some(url("https://github.com/juliano/randomuser-scala")),
    licenses := List("MIT License" -> url("https://github.com/juliano/randomuser-scala/blob/main/LICENSE")),
    developers := List(
      Developer("juliano", "Juliano Alves", "von.juliano@gmail.com", url("https://juliano-alves.com/"))
    )
))

sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "random-user",
    scalaVersion := "3.5.0",
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "dev.zio"                       %% "zio"          % "2.1.16",
      "dev.zio"                       %% "zio-json"     % "0.7.39",
      "com.softwaremill.sttp.client3" %% "zio"          % "3.10.3",
      "com.softwaremill.sttp.client3" %% "zio-json"     % "3.10.3",
      "dev.zio"                       %% "zio-test"     % "2.1.16" % Test,
      "dev.zio"                       %% "zio-test-sbt" % "2.1.16" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
  )
