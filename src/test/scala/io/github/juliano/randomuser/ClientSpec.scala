package io.github.juliano.randomuser

import zio.test.*
import zio.test.TestAspect.*

import params.*

object ClientSpec extends ZIOSpecDefault:
  val charsetsGen: Gen[Any, List[Charsets]] =
    Gen.listOfBounded(1, 4)(Gen.elements(Charsets.values*))

  val nationalitiesGen: Gen[Any, List[Nationality]] =
    Gen.listOfBounded(1, 4)(Gen.elements(Nationality.values*))

  val fieldsGen: Gen[Any, List[Fields]] =
    Gen.listOfBounded(1, 4)(Gen.elements(Fields.values*))

  def spec = suite("RandomUser API client specs")(
    test("default params") {
      for content <- Client.fetch()
      yield assertTrue(content.results.length == 1 && content.info.isDefined)
    },
    test("results") {
      check(Gen.int(1, 100)) { results =>
        for content <- Client.fetch(Params(results = results))
        yield assertTrue(content.results.length == results)
      }
    } @@ samples(3),
    test("gender - only male") {
      for content <- Client.fetch(Params(results = 10).male)
      yield assertTrue(content.results.forall(_.gender.contains("male")))
    },
    test("gender - only female") {
      for content <- Client.fetch(Params(results = 10).female)
      yield assertTrue(content.results.forall(_.gender.contains("female")))
    },
    test("password - max characters") {
      check(charsetsGen, Gen.int(8, 100)) { (cs, max) =>
        for
          content <- Client.fetch(Params(results = 4).password(max, cs.head, cs.tail*))
          passwords = content.results.flatMap(_.login.map(_.password))
        yield assertTrue(passwords.forall(_.length <= max))
      }
    } @@ samples(3),
    test("password - min and max characters") {
      check(charsetsGen, Gen.int(8, 40), Gen.int(41, 100)) { (cs, min, max) =>
        for
          content <- Client.fetch(Params(results = 4).password(min, max, cs.head, cs.tail*))
          passwords = content.results.flatMap(_.login.map(_.password))
        yield assertTrue(passwords.forall(_.length >= min && passwords.forall(_.length <= max)))
      }
    } @@ samples(3),
    test("nationalities") {
      check(nationalitiesGen) { nats =>
        for
          content <- Client.fetch(Params(results = 4).nationalities(nats*))
          nationalities = content.results.flatMap(_.nat)
        yield assertTrue(nationalities.forall(n => nats.map(_.toString).contains(n)))
      }
    } @@ samples(3),
    test("seed and page") {
      check(Gen.alphaNumericStringBounded(1, 64)) { seed =>
        for
          first  <- Client.fetch(Params(results = 4).seed(seed, 2))
          second <- Client.fetch(Params(results = 4).seed(seed, 2))
        yield assertTrue(first.results == second.results)
      }
    } @@ samples(3),
    test("include fields") {
      check(fieldsGen) { included =>
        for
          content <- Client.fetch(Params(results = 4).including(included*))
          excluded = Fields.values.filterNot(included.contains).toList
        yield assertPresence(content, included) && assertAbsence(content, excluded)
      }
    } @@ samples(3),
    test("exclude fields") {
      check(fieldsGen) { excluded =>
        for
          content <- Client.fetch(Params(results = 4).excluding(excluded*))
          included = Fields.values.filterNot(excluded.contains).toList
        yield assertAbsence(content, excluded) && assertPresence(content, included)
      }
    } @@ samples(3),
    test("noinfo") {
      for content <- Client.fetch(Params(2).noInfo)
      yield assertTrue(content.results.length == 2 && content.info.isEmpty)
    }
  ).provideShared(Client.default) @@ nondeterministic

  def assertPresence(content: Content, fields: List[Fields]): TestResult =
    assertTrue(
      content.results.forall { r =>
        fields.forall {
          case Fields.gender     => r.gender.nonEmpty
          case Fields.name       => r.name.nonEmpty
          case Fields.location   => r.location.nonEmpty
          case Fields.email      => r.email.nonEmpty
          case Fields.login      => r.login.nonEmpty
          case Fields.dob        => r.dob.nonEmpty
          case Fields.registered => r.registered.nonEmpty
          case Fields.phone      => r.phone.nonEmpty
          case Fields.cell       => r.cell.nonEmpty
          case Fields.id         => r.id.nonEmpty
          case Fields.picture    => r.picture.nonEmpty
          case Fields.nat        => r.nat.nonEmpty
        }
      }
    )

  def assertAbsence(content: Content, fields: List[Fields]): TestResult =
    assertTrue(
      content.results.forall { r =>
        fields.forall {
          case Fields.gender     => r.gender.isEmpty
          case Fields.name       => r.name.isEmpty
          case Fields.location   => r.location.isEmpty
          case Fields.email      => r.email.isEmpty
          case Fields.login      => r.login.isEmpty
          case Fields.dob        => r.dob.isEmpty
          case Fields.registered => r.registered.isEmpty
          case Fields.phone      => r.phone.isEmpty
          case Fields.cell       => r.cell.isEmpty
          case Fields.id         => r.id.isEmpty
          case Fields.picture    => r.picture.isEmpty
          case Fields.nat        => r.nat.isEmpty
        }
      }
    )
