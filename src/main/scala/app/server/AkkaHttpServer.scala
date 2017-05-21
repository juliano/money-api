package app.server

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.{ IncomingConnection, ServerBinding }
import akka.http.scaladsl.server.Route._
import akka.stream.scaladsl.{ Sink, Source }
import app.config.ServerSettings
import app.routes.Routes

import scala.concurrent.Future
import scala.io.StdIn

object AkkaHttpServer extends App {

  import ServerSettings._

  val server: Source[IncomingConnection, Future[ServerBinding]] =
    Http(actorSystem).bind(httpInterface, httpPort)

  log.info(s"\nAkka HTTP Server - Version ${actorSystem.settings.ConfigVersion} - running at http://$httpInterface:$httpPort/")

  val handler: Future[ServerBinding] =
    server
      .to(
        Sink.foreach {
          connection ⇒
            connection.handleWithAsyncHandler(asyncHandler(Routes.availableRoutes))
        }
      )
      .run()

  handler.failed.foreach { case ex: Exception ⇒ log.error(ex, "Failed to bind to {}:{}", httpInterface, httpPort) }

  StdIn.readLine(s"\nPress RETURN to stop...")

  handler
    .flatMap(binding ⇒ binding.unbind())
    .onComplete(_ ⇒ actorSystem.terminate())
}
