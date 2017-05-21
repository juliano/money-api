package app.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ FreeSpec, Matchers }

class RoutesSpec extends FreeSpec with Matchers with ScalatestRouteTest {

  "GET /api/v1/accounts/" in {
    Get("/api/v1/accounts/") ~> Routes.availableRoutes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "GET /api/v1/accounts/:id" in {
    Get("/api/v1/accounts/1") ~> Routes.availableRoutes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "GET /api/v1/accounts/:id/transactions" in {
    Get("/api/v1/accounts/1/transactions") ~> Routes.availableRoutes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "GET /api/v1/accounts/:id/transactions/:tid" in {
    Get("/api/v1/accounts/1/transactions/1") ~> Routes.availableRoutes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "POST /api/v1/accounts" in {
    newAccountPostRequest ~> Routes.availableRoutes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "POST /api/v1/accounts/1/send" in {
    newTransactionPostRequest ~> Routes.availableRoutes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }
}
