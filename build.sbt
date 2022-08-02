import LessKeys.less

ThisBuild / scalaVersion := "2.13.8"

lazy val `foundry-vtt-map` = project
  .in(file("."))
  .settings(
    name := "Modern Adventuring and Plunder",
    libraryDependencies ++= Seq(
      "org.scala-js"     %%% "scalajs-dom"             % "1.1.0",
      "michalzc.foundry" %%% "foundry-vtt-types-scala" % "0.1.0-SNAPSHOT",
    ),
    scalaJSUseMainModuleInitializer := true,
    Assets / less / includeFilter := "*.less",
  )
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(SbtWeb)
