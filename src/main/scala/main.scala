import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import resource.RoutesResource

@main
def main(): Unit = {

  // https://doc.akka.io/docs/akka-http/current/introduction.html
  implicit val actorSystem: ActorSystem[Any] = ActorSystem(Behaviors.empty, "akka-http")

  Http().newServerAt("localhost", 8080).bind(RoutesResource.allRoutesUnified())
}