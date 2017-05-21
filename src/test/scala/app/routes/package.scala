package app

import akka.http.scaladsl.model._
import akka.util.ByteString

package object routes {

  val accountJson = ByteString(
    s"""
       |{
       |    "initialAmount" : 500.00
       |}
        """.stripMargin
  )

  val newAccountPostRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/api/v1/accounts",
    entity = HttpEntity(MediaTypes.`application/json`, accountJson)
  )

  val transactionJson = ByteString(
    s"""
       |{
       |    "destinationId": 2,
       |	  "amount" : 50.00
       |}
        """.stripMargin
  )

  val newTransactionPostRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/api/v1/accounts/1/transfer",
    entity = HttpEntity(MediaTypes.`application/json`, transactionJson)
  )
}
