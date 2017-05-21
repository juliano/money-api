package app.domain

import java.time.LocalDateTime.now

import org.scalatest.{ BeforeAndAfterAll, FreeSpec, Matchers }

import scala.concurrent.Await
import scala.concurrent.duration._

class TransactionsSpec extends FreeSpec with Matchers with BeforeAndAfterAll {

  import testContext._

  val acc1 = Account(1L, BigDecimal(400.0), now())
  val acc2 = Account(2L, BigDecimal(200.0), now())
  val acc3 = Account(3L, BigDecimal(100.0), now())

  val transaction1 = Transaction(1L, acc1.id, acc2.id, BigDecimal(100))
  val transaction2 = Transaction(2L, acc2.id, acc3.id, BigDecimal(50))

  override def beforeAll = {
    testContext.transaction {
      testContext.run(query[Transaction].delete)
      testContext.run(quote(query[Account].insert(lift(acc1))))
      testContext.run(quote(query[Account].insert(lift(acc2))))
      testContext.run(quote(query[Transaction].insert(lift(transaction1))))
      testContext.run(quote(query[Transaction].insert(lift(transaction2))))
    }
    ()
  }

  "list transactions of specific account" in {
    val transactions = Await.result(Transactions.from(acc1.id), 1 second)

    transactions should contain only (transaction1)
  }

  "find specific transaction of an account" in {
    val transaction = Await.result(Transactions.get(acc1.id, 1), 1 second)

    transaction.get should equal(transaction1)
  }

  "register a transaction between accounts" in {
    val transaction = Transaction(5, acc1.id, acc3.id, BigDecimal(25))
    testContext.run(quote(query[Transaction].insert(lift(transaction))))

    val persistedTransaction = Await.result(Transactions.get(acc1.id, 5), 1 second)
    persistedTransaction.get should equal(transaction)
  }
}
