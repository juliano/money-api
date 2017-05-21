package app.routes

import akka.actor.ActorSystem

case class ApiMessage(message: String)

object ApiStatusMessages {

  def currentStatus()(implicit actorSystem: ActorSystem) = {
    s"service: ${actorSystem.name} | uptime: ${(actorSystem.uptime.toFloat / 3600).formatted("%10.2f")} hours"
  }

  val unknownException = "An error occurred during the request."
}
