package app.domain

import io.getquill.{ H2JdbcContext, SnakeCase }

import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

object MoneyTransfer {

  val ctx = new H2JdbcContext[SnakeCase]("ctx")
  import ctx._

  def transfer(sourceId: Long, request: TransactionRequest): Future[Transaction] = {
    val source = Await.result(Accounts.get(sourceId), 1 second).get
    val destination = Await.result(Accounts.get(request.destinationId), 1 second).get

    if (source.canTransfer(request.amount)) {
      transfer(source, destination, request.amount)
      Transactions.create(sourceId, destination.id, request.amount)
    } else {
      Future.failed(new IllegalStateException("Not enough money!"))
    }
  }

  private def transfer(source: Account, destination: Account, amount: BigDecimal): Unit = {
    ctx.transaction {
      ctx.run(takeFromSource(source, amount))
      ctx.run(addToDestination(destination, amount))
    }
  }

  private def takeFromSource(account: Account, amount: BigDecimal) = quote {
    query[Account].filter(_.id == lift(account.id)).update(_.amount → lift(account.amount - amount))
  }

  private def addToDestination(account: Account, amount: BigDecimal) = quote {
    query[Account].filter(_.id == lift(account.id)).update(_.amount → lift(account.amount + amount))
  }
}
