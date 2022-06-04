import $ivy.`com.lihaoyi::mill-contrib-versionfile:`
import $file.yaml
import mill._
import mill.api.Result
import mill.api.Result.{Exception, OuterStack, Success}
import mill.scalajslib.ScalaJSModule
import mill.scalalib._
import mill.contrib.versionfile.VersionFileModule

object versionFile extends VersionFileModule

object ModernAdventuringAndPlundering extends ScalaJSModule {
  def scalaVersion   = "2.13.8"
  def scalaJSVersion = "1.10.0"

  override def ivyDeps = Agg(
    ivy"michalzc.foundry::foundry-vtt-types-scala_sjs1:0.1.0-SNAPSHOT",
    ivy"com.outr::scribe_sjs1:3.8.0",
  )

  val yamlSourceDir = millSourcePath / "yaml"
  def yamlSources   = T.sources(yamlSourceDir)

  def allJsons = T {
    yaml
      .doYaml(yamlSources(), yamlSourceDir, T.dest, versionFile.currentVersion().toString())
      .fold[Result[Seq[PathRef]]](
        e => Exception(e, new OuterStack(Seq.empty)),
        l => Success(l),
      )
  }

  def devBuild = T {
    val report = fastLinkJS()
    val jsFiles: Seq[PathRef] = report.publicModules.toList.flatMap(module =>
      List(Option(report.dest.path / module.jsFileName), module.sourceMapName.map(sm => report.dest.path / sm)).flatten.map(PathRef(_)),
    )
    val allFiles = jsFiles ++ resources() ++ allJsons()

    allFiles.foreach(println)

  }

}

def devBuild = T {
  val outDest = T.dest
  ModernAdventuringAndPlundering.devBuild()
  println(outDest)
}
