package io.github.juliano.randomuser

import zio.json.{ DeriveJsonDecoder, JsonDecoder }

import java.time.OffsetDateTime
import java.util.UUID

final case class Content(results: List[Content.Result], info: Option[Content.Info])

object Content:
  given JsonDecoder[Content] = DeriveJsonDecoder.gen

  final case class Result(
      gender: Option[String],
      name: Option[Name],
      location: Option[Location],
      email: Option[String],
      login: Option[Login],
      dob: Option[DateAndAge],
      registered: Option[DateAndAge],
      phone: Option[String],
      cell: Option[String],
      id: Option[Id],
      picture: Option[Picture],
      nat: Option[String]
  )

  object Result:
    given JsonDecoder[Result] = DeriveJsonDecoder.gen

  final case class Name(title: String, first: String, last: String)

  object Name:
    given JsonDecoder[Name] = DeriveJsonDecoder.gen

  final case class Location(
      street: Street,
      city: String,
      state: String,
      country: String,
      postcode: String | Int,
      coordinates: Coordinates,
      timezone: Timezone
  )

  object Location:
    given JsonDecoder[String | Int] = JsonDecoder[String].orElse(JsonDecoder[Int].map(_.toString))

    given JsonDecoder[Location] = DeriveJsonDecoder.gen

  final case class Street(number: Int, name: String)

  object Street:
    given JsonDecoder[Street] = DeriveJsonDecoder.gen

  final case class Coordinates(latitude: String, longitude: String)

  object Coordinates:
    given JsonDecoder[Coordinates] = DeriveJsonDecoder.gen

  final case class Timezone(offset: String, description: String)

  object Timezone:
    given JsonDecoder[Timezone] = DeriveJsonDecoder.gen

  final case class Login(
      uuid: UUID,
      username: String,
      password: String,
      salt: String,
      md5: String,
      sha1: String,
      sha256: String
  )

  object Login:
    given JsonDecoder[Login] = DeriveJsonDecoder.gen

  final case class DateAndAge(date: OffsetDateTime, age: Int)

  object DateAndAge:
    given JsonDecoder[DateAndAge] = DeriveJsonDecoder.gen

  final case class Id(name: String, value: Option[String])

  object Id:
    given JsonDecoder[Id] = DeriveJsonDecoder.gen

  final case class Picture(large: String, medium: String, thumbnail: String)

  object Picture:
    given JsonDecoder[Picture] = DeriveJsonDecoder.gen

  final case class Info(seed: String, results: Int, page: Int, version: String)

  object Info:
    given JsonDecoder[Info] = DeriveJsonDecoder.gen
