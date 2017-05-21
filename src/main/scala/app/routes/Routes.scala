package app.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.domain._
import de.heikoseeberger.akkahttpcirce.CirceSupport._

object Routes extends BaseRoute with ResponseFactory {

  import io.circe.generic.auto._
  import io.circe.java8.time._

  protected def accountDirectives: Route =
    pathPrefix("accounts") {
      get {
        pathEndOrSingleSlash {
          sendResponse(Accounts.all)
        } ~
          pathPrefix(LongNumber) { accountId ⇒
            rejectEmptyResponse {
              pathEndOrSingleSlash {
                sendResponse(Accounts.get(accountId))
              }
            } ~
              pathPrefix("transactions") {
                rejectEmptyResponse {
                  pathEndOrSingleSlash {
                    sendResponse(Transactions.from(accountId))
                  } ~
                    path(LongNumber) { transactionId ⇒
                      sendResponse(Transactions.get(accountId, transactionId))
                    }
                }
              }
          }
      } ~
        post {
          pathEndOrSingleSlash {
            decodeRequest {
              entity(as[AccountRequest]) { request ⇒
                sendResponse(Accounts.add(request))
              }
            }
          } ~
            pathPrefix(LongNumber) { accountId ⇒
              path("transfer") {
                decodeRequest {
                  entity(as[TransactionRequest]) { request ⇒
                    sendResponse(MoneyTransfer.transfer(accountId, request))
                  }
                }
              }
            }
        }
    }

  protected lazy val apiV1: Route =
    api(dsl = logRequestResult("log-service") {
      this.accountDirectives
    }, prefix = true, version = "v1")

  def availableRoutes: Route = apiV1
}
