package io.github.juliano.randomuser

object params:
  final case class Params(
      results: Int = 1,
      gender: Option[Gender] = None,
      password: Option[Password] = None,
      nat: Option[Set[Nationality]] = None,
      seed: Option[Seed] = None,
      inc: Option[Include] = None,
      exc: Option[Exclude] = None,
      noinfo: Option[String] = None
  ):
    def toMap: Map[String, String] =
      List(
        "results" -> Some(results.toString),
        "gender" -> gender.map(_.toString),
        "password" -> password.map(_.encode),
        "nat" -> nat.map(_.mkString(",")),
        "seed" -> seed.map(_.value),
        "page" -> seed.flatMap(_.page).map(_.toString),
        "inc" -> inc.map(_.fields.mkString(",")),
        "exc" -> exc.map(_.fields.mkString(",")),
        "noinfo" -> noinfo
      ).collect { case (field, Some(value)) =>
        field -> value
      }.toMap

    def male: Params   = copy(gender = Some(Gender.male))
    def female: Params = copy(gender = Some(Gender.female))

    def password(charset: Charsets, charsets: Charsets*): Params =
      copy(password = Some(Password(charsets.toSet + charset, None, None)))
    def password(max: Int, charset: Charsets, charsets: Charsets*): Params =
      copy(password = Some(Password(charsets.toSet + charset, None, Some(max))))
    def password(min: Int, max: Int, charset: Charsets, charsets: Charsets*): Params =
      copy(password = Some(Password(charsets.toSet + charset, Some(min), Some(max))))

    def nationalities(nats: Nationality*): Params = copy(nat = Some(nats.toSet))

    def seed(value: String): Params            = copy(seed = Some(Seed(value, None)))
    def seed(value: String, page: Int): Params = copy(seed = Some(Seed(value, Some(page))))

    def including(fields: Fields*): Params = copy(inc = Some(Include(fields.toSet)))
    def excluding(fields: Fields*): Params = copy(exc = Some(Exclude(fields.toSet)))

    def noInfo: Params = copy(noinfo = Some(""))

  enum Gender:
    case male, female

  final case class Password(charsets: Set[Charsets], min: Option[Int], max: Option[Int]):
    def encode: String =
      val minLength = min.map(_.toString + "-").getOrElse("")
      charsets.mkString(",") + s",$minLength${max.getOrElse("")}"

  enum Charsets:
    case special, upper, lower, number

  enum Nationality:
    case AU, BR, CA, CH, DE, DK, ES, FI, FR, GB, IE, IN, IR, MX, NL, NO, NZ, RS, TR, UA, US

  case class Seed(value: String, page: Option[Int])

  enum Fields:
    case gender, name, location, email, login, dob, registered, phone, cell, id, picture, nat

  final case class Include(fields: Set[Fields])

  final case class Exclude(fields: Set[Fields])
