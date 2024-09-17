package io.github.juliano.randomuser

import sttp.client3.*
import sttp.client3.ziojson.asJson
import sttp.model.HeaderNames
import sttp.model.MediaType.ApplicationJson
import sttp.model.Uri
import zio.{ IO, Task, ZIO }

import params.Params

final case class ClientLive(backend: SttpBackend[Task, Any]) extends Client:
  val baseUri: Uri = uri"https://randomuser.me/api/"

  def fetch(params: Params): IO[ApiException, Content] =
    basicRequest
      .contentType(ApplicationJson)
      .response(asJson[Content])
      .get(baseUri.addParams(params.toMap))
      .send(backend)
      .flatMap(r => ZIO.fromEither(r.body))
      .mapError(ApiException.handle)
