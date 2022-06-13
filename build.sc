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
    ivy"org.scala-js::scalajs-dom_sjs1:1.1.0",
  )

  def yamlSourceDir = T.sources(millSourcePath / "yaml")
  def moduleYamls = T {
    yamlSourceDir().flatMap(p => os.walk(p.path)).map(PathRef(_))
  }

  def moduleJsons = T {
    yaml
      .doYaml(moduleYamls(), yamlSourceDir().head.path, T.dest, versionFile.currentVersion().toString())
      .fold[Result[PathRef]](
        e => Exception(e, new OuterStack(Seq.empty)),
        l => Success(PathRef(T.dest)),
      )
  }

  def devBuild = T {
    val report = fastLinkJS()
    val jsons  = moduleJsons()
    val jsFiles: Seq[(PathRef, String)] = report.publicModules.toList.flatMap(module =>
      List(Option(report.dest.path / module.jsFileName), module.sourceMapName.map(sm => report.dest.path / sm)).flatten
        .map(PathRef(_))
        .map(p => p -> s"modules/${p.path.last}"),
    )

    def res = resources().flatMap(p => os.walk(p.path).filter(_.toIO.isFile).map(rp => (PathRef(rp), rp.relativeTo(p.path).toString())))

    jsFiles ++ res ++ os.walk(jsons.path).map(p => PathRef(p) -> p.last)
  }

}

def devBuild = T {
  ModernAdventuringAndPlundering.devBuild().map { case (path, destPath) =>
    val fullDestPath = T.dest / RelPath(destPath)
    os.copy(path.path, fullDestPath, createFolders = true)
  }
  PathRef(T.dest)
}
