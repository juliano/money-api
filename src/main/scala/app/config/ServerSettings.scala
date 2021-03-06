package app.config

import akka.actor.ActorSystem
import akka.event.{ LogSource, Logging }
import akka.stream.ActorMaterializer
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor

trait ServerSettings {

  lazy private val config: Config = ConfigFactory.load()
  private val httpConfig: Config = config.getConfig("http")
  val httpInterface: String = httpConfig.getString("interface")
  val httpPort: Int = httpConfig.getInt("port")

  implicit val actorSystem: ActorSystem = ActorSystem("transporter-scala")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  private implicit val logSource: LogSource[ServerSettings] = (t: ServerSettings) ⇒ t.getClass.getSimpleName
  private def logger(implicit logSource: LogSource[_ <: ServerSettings]) = Logging(actorSystem, this.getClass)

  implicit val log = logger
}

object ServerSettings extends ServerSettings
