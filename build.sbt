import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile

inThisBuild(List(
    organization := "io.github.juliano",
    homepage := Some(url("https://github.com/juliano/randomuser-scala")),
    licenses := List("MIT License" -> url("https://github.com/juliano/randomuser-scala/blob/main/LICENSE")),
    developers := List(
      Developer("juliano", "Juliano Alves", "von.juliano@gmail.com", url("https://juliano-alves.com/"))
    )
))

lazy val root = project
  .in(file("."))
  .settings(
    name         := "randomuser",
    scalaVersion := "3.5.0",
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "dev.zio"                       %% "zio"          % "2.1.9",
      "dev.zio"                       %% "zio-json"     % "0.7.3",
      "com.softwaremill.sttp.client3" %% "zio"          % "3.9.8",
      "com.softwaremill.sttp.client3" %% "zio-json"     % "3.9.8",
      "dev.zio"                       %% "zio-test"     % "2.1.9" % Test,
      "dev.zio"                       %% "zio-test-sbt" % "2.1.9" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    scalacOptions := Seq(
      "-deprecation",
      "-encoding",
      "utf-8",
      "-explain-types",
      "-feature",
      "-unchecked",
      "-language:postfixOps",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:existentials",
      "-Xfatal-warnings",
      "-Xkind-projector",
      "-Yretain-trees"
    )
  )
