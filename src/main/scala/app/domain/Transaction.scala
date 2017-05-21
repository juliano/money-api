package app.domain

import java.time.LocalDateTime
import java.time.LocalDateTime.now

case class Transaction(id: Long, createdAt: LocalDateTime, source: Long, destination: Long, amount: BigDecimal)

object Transaction {
  def apply(id: Long, sourceId: Long, destinationId: Long, amount: BigDecimal) =
    new Transaction(id, now(), sourceId, destinationId, amount)
}

case class TransactionRequest(destinationId: Long, amount: BigDecimal)
