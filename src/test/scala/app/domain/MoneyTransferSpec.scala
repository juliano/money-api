package app.domain

import java.time.LocalDateTime.now

import org.scalatest.{ BeforeAndAfterAll, FreeSpec, Matchers }

import scala.concurrent.Await
import scala.concurrent.duration._

class MoneyTransferSpec extends FreeSpec with Matchers with BeforeAndAfterAll {

  import testContext._

  val source = Account(1L, BigDecimal(400.0), now())
  val destination = Account(2L, BigDecimal(200.0), now())

  override def beforeAll = {
    testContext.transaction {
      testContext.run(query[Account].delete)
      testContext.run(quote(query[Account].insert(lift(source))))
      testContext.run(quote(query[Account].insert(lift(destination))))
    }
    ()
  }

  "execute a transfer between accounts" in {
    val request = TransactionRequest(destination.id, BigDecimal(400))
    MoneyTransfer.transfer(source.id, request)

    val updatedSource = Await.result(Accounts.get(source.id), 1 second).get
    val updatedDestination = Await.result(Accounts.get(destination.id), 1 second).get

    updatedSource.amount shouldEqual 0
    updatedDestination.amount shouldEqual 600
  }
}
