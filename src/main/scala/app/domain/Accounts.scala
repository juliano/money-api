package app.domain

import app.config.ServerSettings._
import io.getquill.{ H2JdbcContext, SnakeCase }

import scala.concurrent.Future

object Accounts {

  val ctx = new H2JdbcContext[SnakeCase]("ctx")
  import ctx._

  def all: Future[List[Account]] = Future {
    ctx.run(query[Account])
  }

  def add(request: AccountRequest) = Future[Account] {
    val account = Account(nextId, request)
    val insert = quote(query[Account].insert(lift(account)))
    ctx.run(insert)
    account
  }

  def get(id: Long) = Future[Option[Account]] {
    ctx.run(quote {
      query[Account].filter(_.id == lift(id))
    }).headOption
  }

  private def nextId: Long = {
    val ids = quote(query[Account].map(_.id))
    ctx.run(ids.max) match {
      case Some(id) ⇒ id + 1
      case None     ⇒ 1
    }
  }
}
