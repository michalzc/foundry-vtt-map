import com.typesafe.sbt.less.Import.LessKeys.less
import com.typesafe.sbt.web.Import.Assets
import org.scalajs.linker.interface.Report
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.{fastLinkJS, scalaJSLinkerOutputDirectory}
import sbt.Keys._
import sbt.io.Path.{flat, rebase}
import sbt.{AutoPlugin, Def, File, taskKey, _}
import _root_.io.circe.yaml.parser
import _root_.io.circe.Json

object DistPlugin extends AutoPlugin {
  override def requires = ScalaJSPlugin
  override def trigger  = allRequirements

  object autoImport {
    lazy val devDist                  = taskKey[Seq[(File, File)]]("Prepare local version of system to include in FoundryVTT")
    lazy val devDistOutput            = settingKey[File]("Location for development dist output")
    lazy val yamlSourceDirectory      = settingKey[File]("Source directory with YAML files")
    lazy val distOutputDirectory      = settingKey[File]("Location for distribution output")
    lazy val distBuildOptputDirectory = settingKey[File]("Location for distribution build directory")
    lazy val distFile                 = settingKey[File]("Location for distribution file")
    lazy val distSystemFile           = settingKey[File]("Location for distribution system file")
    lazy val mainYamlFilter           = settingKey[FileFilter]("Filter for main yaml files (template and system)")
  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {

    inConfig(Compile)(
      Seq(
        devDist                  := buildTask(fastLinkJS, devDistOutput, "dev-dist").value ++ buildMainJsonsTask(devDistOutput, "dev-dist").value,
        devDistOutput            := target.value / s"${moduleName.value}-dev",
        yamlSourceDirectory      := sourceDirectory.value / "yaml",
        distOutputDirectory      := target.value / s"${moduleName.value}",
        distBuildOptputDirectory := distOutputDirectory.value / "build",
        distFile                 := distOutputDirectory.value / s"${moduleName.value}.zip",
        distSystemFile           := distOutputDirectory.value / s"${moduleName.value}.json",
        mainYamlFilter           := "system.yaml" | "template.yaml",
        ),
    )
  }

  def setVersion(version: String)(json: Json) =
      json.hcursor.downField("version").withFocus(_.mapString(_ => version)).top.getOrElse(json)

  def buildMainJsonsTask(outputDirectory: SettingKey[File], name: String) =
    Def.task {
      val yamlFiles = (yamlSourceDirectory.value * mainYamlFilter.value).get
      val mappedFiles = yamlFiles.map { file =>
        val jsonContent = IO.reader(file)(parser.parse).map(setVersion(version.value)).map(_.spaces2) match {
          case Left(error) => throw error
          case Right(js) => js
        }
        val outFile = outputDirectory.value / file.getName.replace(".yaml", ".json")
        IO.write(outFile, jsonContent)
        file -> outFile
      }
      val cacheStore = streams.value.cacheStoreFactory make s"${name}-main-yamls"
      Sync.sync(cacheStore)(mappedFiles)
      mappedFiles
    }

  def buildTask(link: TaskKey[Attributed[Report]], outputDirectory: SettingKey[File], name: String) =
    Def.task {
      val jsOutDir = (link / scalaJSLinkerOutputDirectory).value
      val scripts = link.value.data.publicModules.toList.flatMap { module =>
        List(Option(jsOutDir / module.jsFileName), module.sourceMapName.map(jsOutDir / _))
      }.flatten
      val lessOutDir                      = (Assets / less / resourceManaged).value
      val files                           = (Assets / less).value ++ resources.value ++ scripts
      val t                               = outputDirectory.value
      val dirs                            = (lessOutDir +: jsOutDir +: resourceDirectories.value).toSet
      val s                               = streams.value
      val cacheStore                      = s.cacheStoreFactory make name
      val flt: File => Option[File]       = flat(t)
      val transform: File => Option[File] = (f: File) => rebase(dirs, t)(f).orElse(flt(t))
      val mappings: Seq[(File, File)] = files.flatMap {
        case r if !dirs(r) => transform(r).map(r -> _)
        case _             => None
      }
      s.log.debug("Copying scripts: " + mappings.mkString("\n\t", "\n\t", ""))
      Sync.sync(cacheStore)(mappings)
      mappings
    }
}
