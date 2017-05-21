package app

import io.getquill.{ H2JdbcContext, SnakeCase }

package object domain {

  object testContext extends H2JdbcContext[SnakeCase]("ctx")
}
