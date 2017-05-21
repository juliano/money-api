package app.domain

import java.time.LocalDateTime.now

import org.scalatest.{ BeforeAndAfterAll, FreeSpec, Matchers }

import scala.concurrent.Await
import scala.concurrent.duration._

class AccountsSpec extends FreeSpec with Matchers with BeforeAndAfterAll {

  import testContext._

  val account1 = Account(1L, BigDecimal(100.0), now())
  val account2 = Account(2L, BigDecimal(200.0), now())

  override def beforeAll = {
    testContext.transaction {
      testContext.run(query[Account].delete)
      testContext.run(quote(query[Account].insert(lift(account1))))
      testContext.run(quote(query[Account].insert(lift(account2))))
    }
    ()
  }

  "list all accounts" in {
    val accounts = Await.result(Accounts.all, 1 second)

    accounts should contain allOf (account1, account2)
  }

  "find by id" in {
    val account = Await.result(Accounts.get(1L), 1 second).get

    account should equal(account1)
  }

  "create a new Account with initial amount" in {
    val account = Account(5, BigDecimal(500), now())
    testContext.run(quote(query[Account].insert(lift(account))))

    val persistedAccount = Await.result(Accounts.get(5), 1 second).get
    persistedAccount should equal(account)
  }
}
