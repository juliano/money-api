package app.domain

import app.config.ServerSettings._
import io.getquill.{ H2JdbcContext, SnakeCase }

import scala.concurrent.Future

object Transactions {

  val ctx = new H2JdbcContext[SnakeCase]("ctx")
  import ctx._

  def from(accountId: Long): Future[List[Transaction]] = Future {
    ctx.run(quote {
      query[Transaction].filter(t ⇒ t.source == lift(accountId) || t.destination == lift(accountId))
    })
  }

  def get(accountId: Long, transactionId: Long): Future[Option[Transaction]] = Future {
    ctx.run(quote {
      query[Transaction].filter(t ⇒ t.source == lift(accountId) && t.id == lift(transactionId))
    }).headOption
  }

  def create(sourceId: Long, destinationId: Long, amount: BigDecimal) = Future[Transaction] {
    val transaction = Transaction(nextId, sourceId, destinationId, amount)
    val insert = quote(query[Transaction].insert(lift(transaction)))
    ctx.run(insert)
    transaction
  }

  private def nextId: Long = {
    val ids = quote(query[Transaction].map(_.id))
    ctx.run(ids.max) match {
      case Some(id) ⇒ id + 1
      case None     ⇒ 1
    }
  }
}
