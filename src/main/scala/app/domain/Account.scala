package app.domain

import java.time.LocalDateTime
import java.time.LocalDateTime.now

case class Account(id: Long, amount: BigDecimal, createdAt: LocalDateTime) {

  def canTransfer(amount: BigDecimal) = this.amount >= amount
}

object Account {
  def apply(id: Long, request: AccountRequest): Account = new Account(id, request.initialAmount, now())
}

case class AccountRequest(initialAmount: BigDecimal)
