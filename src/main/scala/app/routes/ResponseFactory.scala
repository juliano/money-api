package app.routes

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import app.config.ServerSettings

import scala.concurrent.Future
import scala.util.{ Failure, Success }

trait ResponseFactory {

  import ServerSettings._
  import io.circe.generic.auto._

  def sendResponse[T](eventualResult: Future[T])(implicit marshaller: T ⇒ ToResponseMarshallable): Route = {
    onComplete(eventualResult) {
      case Success(result) ⇒
        complete(result)
      case Failure(e) ⇒ e match {
        case x: IllegalStateException ⇒
          complete(ToResponseMarshallable(Forbidden → ApiMessage(x.getMessage)))
        case _ ⇒
          log.error(s"Error: ${e.toString}")
          complete(ToResponseMarshallable(InternalServerError → ApiMessage(ApiStatusMessages.unknownException)))
      }

    }
  }
}
