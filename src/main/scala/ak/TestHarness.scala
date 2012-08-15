package ak
import akka.camel.{Consumer, CamelMessage}
import akka.actor.{Props, ActorSystem, ActorLogging}
import akka.util.Timeout
import akka.util.duration._
import akka.actor.Actor
import akka.camel.CamelMessage


object TestHarnessApp extends App {
  val sys = ActorSystem("TestHarnessApp")
  sys.actorOf(Props[HttpConsumer], "EndPoint-foo")
  sys.awaitTermination()
}

class HttpConsumer extends Consumer {
  def endpointUri = "jetty://http://0.0.0.0:8888/foo"
  implicit val timeout = Timeout(30 seconds)
  def receive = {
    case msg : CamelMessage => {
      sender ! soap("Foo")
    }
  }
  def soap(message: String) = {
    <soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope" soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
      <soap:Body xmlns:m="http://www.foo.org/foo">
        <m:message>{message}</m:message>
      </soap:Body>
    </soap:Envelope>
  }
}

