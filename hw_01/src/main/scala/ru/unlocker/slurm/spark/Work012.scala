package ru.unlocker.slurm.spark

import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser.decode
import io.circe.syntax._

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}

object Work012 {

  // Классы оболочки
  case class CountryName(official: String)

  case class CountrySource(name: CountryName, area: Float, region: String, capital: List[String])

  case class CountryDto(name: String, area: Float, capital: String)

  implicit val countryNameDecoder: Decoder[CountryName] = deriveDecoder[CountryName]
  implicit val countrySourceDecoder: Decoder[CountrySource] = deriveDecoder[CountrySource]
  implicit val countryDtoEncoder: Encoder.AsObject[CountryDto] = deriveEncoder[CountryDto]

  /**
   * Эмулятор конструкции scala.util.Using для Scala 2.12
   */
  def using[A <: AutoCloseable, B](resource: => A)(block: A => B): B = Try(block(resource)) match {
    case Failure(exception) =>
      resource.close()
      throw exception
    case Success(value) =>
      resource.close()
      value
  }

  /**
   * Конвертирует исходную структуру в целевую.
   */
  def createDto(in: CountrySource): CountryDto = CountryDto(in.name.official, in.area, in.capital.head)

  def main(args: Array[String]): Unit = {
    // зачитывает данные из файла
    using(Source.fromResource("01_countries.json")) { bufSource =>
      // разбор JSON
      val parsingResult: Either[Error, List[CountrySource]] = decode[List[CountrySource]](bufSource.mkString)
      println(s"Результат разбора: ${parsingResult.getClass.getSimpleName}")
      // выборка и сортировка списка
      parsingResult
        .map(
          _.filter(_.region == "Africa")
            .sortWith((x, y) => x.area > y.area)
            .take(10)
            .map(createDto))
        // запись результата
        .foreach { dtos =>
          val filename = "result.json"
          Files.write(Paths.get(filename), dtos.asJson.toString().getBytes(UTF_8))
          println(s"Файл $filename успешно сформирован")
        }
    }
  }

}
