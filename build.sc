import $ivy.`com.lihaoyi::mill-contrib-versionfile:`
import $file.yaml
import mill._
import mill.api.Result
import mill.api.Result.{Exception, OuterStack, Success}
import mill.scalajslib.ScalaJSModule
import mill.scalalib._
import mill.contrib.versionfile.VersionFileModule
import os.RelPath

object versionFile extends VersionFileModule

object ModernAdventuringAndPlundering extends ScalaJSModule {
  def scalaVersion   = "2.13.8"
  def scalaJSVersion = "1.10.0"

  override def ivyDeps = Agg(
    ivy"michalzc.foundry::foundry-vtt-types-scala_sjs1:0.1.0-SNAPSHOT",
    ivy"com.outr::scribe_sjs1:3.8.0",
  )

  val yamlSourceDir             = millSourcePath / "yaml"
  val moduleYamls: Seq[PathRef] = List("system.yaml", "template.yaml").map(yamlSourceDir / _).map(PathRef(_)).filter(_.path.toIO.isFile)

  def moduleJsons = T {
    yaml
      .doYaml(moduleYamls, yamlSourceDir, T.dest, versionFile.currentVersion().toString())
      .fold[Result[Seq[(PathRef, String)]]](
        e => Exception(e, new OuterStack(Seq.empty)),
        l => Success(l),
      )
  }

  def devBuild = T {
    val report = fastLinkJS()
    val jsFiles: Seq[(PathRef, String)] = report.publicModules.toList.flatMap(module =>
      List(Option(report.dest.path / module.jsFileName), module.sourceMapName.map(sm => report.dest.path / sm)).flatten
        .map(PathRef(_))
        .map(p => p -> s"modules/${p.path.last}"),
    )

    val res = resources().flatMap(p => os.walk(p.path).filter(_.toIO.isFile).map(rp => (PathRef(rp), rp.relativeTo(p.path).toString())))

    jsFiles ++ res ++ moduleJsons()
  }

}

def devBuild = T {
  val elements = ModernAdventuringAndPlundering.devBuild()
  val outDir   = T.dest
  elements.map { case (path, destPath) =>
    val fullDestPath = outDir / RelPath(destPath)
    os.copy(path.path, fullDestPath, createFolders = true)
    PathRef(fullDestPath)
  }
}
