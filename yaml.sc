import mill.api.PathRef
import mill._
import scalalib._
import $ivy.`io.circe::circe-yaml:0.14.1`
import io.circe.yaml.parser
import os.{Path, RelPath}
import cats.syntax.traverse._
import cats.instances.list._
import io.circe.{Json, ParsingFailure}
import io.circe.syntax._

def doYaml(sources: Seq[PathRef], basePath: Path, dest: Path, version: String): Either[ParsingFailure, List[(Path, String)]] = {

  def setVersion(baseName: String, json: Json, version: String): Json = baseName match {
    case "system" => json.deepMerge(Map("version" -> version).asJson)
    case _        => json
  }

  sources.toList
    .map(_.path)
    .map(f => (f.relativeTo(basePath), parser.parse(os.read(f))))
    .traverse { case (filePath, json) =>
      val baseName = filePath.baseName
      val outName  = s"$baseName.json"
      val outPath  = dest / s"$baseName.json"
      json.map { json =>
        os.write(outPath, setVersion(baseName, json, version).spaces2)
        outPath -> outName
      }
    }
}
