package io.github.juliano.randomuser

import sttp.client3.SttpBackend
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.*

import params.Params

trait Client:
  def fetch(params: Params = Params()): IO[ApiException, Content]

object Client:
  val layer: URLayer[SttpBackend[Task, Any], Client] =
    ZLayer.fromFunction(ClientLive.apply)

  val default: ULayer[Client] =
    ZLayer.make[Client](HttpClientZioBackend.layer(), layer).orDie

  def fetch(params: Params = Params()): ZIO[Client, ApiException, Content] =
    ZIO.serviceWithZIO(_.fetch(params))
