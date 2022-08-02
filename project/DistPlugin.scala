import com.typesafe.sbt.less.Import.LessKeys.less
import com.typesafe.sbt.web.Import.Assets
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.{fastLinkJS, scalaJSLinkerOutputDirectory}
import sbt.Keys._
import sbt.io.Path.{flat, rebase}
import sbt.{AutoPlugin, Def, File, taskKey, _}

object DistPlugin extends AutoPlugin {
  override def requires = ScalaJSPlugin
  override def trigger  = allRequirements

  object autoImport {
    lazy val devDist       = taskKey[Seq[(File, File)]]("Prepare local version of system to include in FoundryVTT")
    lazy val devDistOutput = settingKey[File]("Location for development dist output")
  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {

    inConfig(Compile)(
      Seq(
        devDist := copyFilesTask.value,
        devDistOutput := target.value / moduleName.value
      ))
  }

  def copyFilesTask =
    Def.task {
      val jsOutDir = (fastLinkJS / scalaJSLinkerOutputDirectory).value
      val scripts = fastLinkJS.value.data.publicModules.toList.flatMap { module =>
        List(Option(jsOutDir / module.jsFileName), module.sourceMapName.map(jsOutDir / _))
      }.flatten
      val lessOutDir = (Assets / less / resourceManaged).value
      val files = (Assets / less).value ++ resources.value ++ scripts
      val t = devDistOutput.value
      val dirs = (lessOutDir +: jsOutDir +: resourceDirectories.value).toSet
      val s = streams.value
      val cacheStore = s.cacheStoreFactory make "dev-dist"
      val flt: File => Option[File] = flat(t)
      val transform: File => Option[File] = (f: File) => rebase(dirs, t)(f).orElse(flt(t))
      val mappings: Seq[(File, File)] = files.flatMap {
        case r if !dirs(r) => transform(r).map(r -> _)
        case _ => None
      }
      s.log.debug("Copying scripts: " + mappings.mkString("\n\t", "\n\t", ""))
      Sync.sync(cacheStore)(mappings)
      mappings
    }

//  def getJsModules(jsDir: File, jsLinkReport: Report): Seq[File] = {
//    jsLinkReport.publicModules.toList.flatMap { module =>
//      List(Option(jsDir / module.jsFileName), module.sourceMapName.map(mn => jsDir / mn))
//    }.flatten
//  }
//
//  def copyFiles(destDir: File, jsModuleFiles: Seq[File], resporceDir: File)(implicit logger: ManagedLogger): File = {
//    mkDir(destDir)
//
////    val resDirCnt = Option(resporceDir.toPath.getNameCount)
//    jsModuleFiles.foreach(copyToDir(_, destDir, None))
////    (resporceDir ** ("*")).get().foreach(copyToDir(_, destDir, resDirCnt))
//
//    destDir
//  }
//
//  def mkDir(dir: File)(implicit logger: ManagedLogger): File = {
//    if(!dir.isDirectory && !dir.mkdirs()) throw new IllegalAccessException(s"Can't create ${dir}")
//    dir
//  }
//
//  def copyToDir(file: File, dir: File, basePathLen: Option[Int])(implicit logger: ManagedLogger): AnyVal = {
//    val out = basePathLen.flatMap { cnt => //FIXME: it's terrible, there must be a better way
//      val path = file.getParentFile.toPath
//      val nameCount = path.getNameCount
//      if(nameCount > cnt) {
//        Option(path.subpath(cnt, nameCount))
//      } else {
//        None
//      }
//    }.map(dir / _.toString / file.getName).getOrElse( dir / file.getName)
//
//    if(file.isFile) IO.copyFile(file, out)
//    else ()
//  }
}
