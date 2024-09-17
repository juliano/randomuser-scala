package io.github.juliano.randomuser

import sttp.client3.*

final case class ApiException(message: String, cause: ResponseException[?, ?])

object ApiException:
  def handle: PartialFunction[Throwable, ApiException] =
    case t: HttpError[?]                => ApiException("Failure communicating with the api", t)
    case e: DeserializationException[?] => ApiException(s"${e.error}\nRaw body: ${e.body}", e)
