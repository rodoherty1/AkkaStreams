package io.rob.websockets.helloworld

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest, WebSocketUpgradeResponse}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Future

/**
  * See http://doc.akka.io/docs/akka-http/10.0.9/scala/http/client-side/websocket-support.html
  */
object HelloWorldWebsocketsClient extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  import system.dispatcher


  val outgoing: Source[TextMessage, ActorRef] = Source.actorRef[TextMessage](10, OverflowStrategy.dropTail)

  val incoming = Sink.foreach[Message] {
    case tm: TextMessage.Strict => println(tm)
  }

  private lazy val flow: Flow[Message, Message, Future[WebSocketUpgradeResponse]] = Http().webSocketClientFlow(WebSocketRequest("ws://localhost:8080/hello"))

  val ((sourceActor, upgradeResponse), done: Future[Done]) =
    outgoing.viaMat(flow)(Keep.both)
      .toMat(incoming)(Keep.both)
      .run

  val connected: Future[Done] = upgradeResponse.flatMap { upgrade =>
    if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
      Future.successful(Done)
    } else {
      throw new IllegalStateException(s"Connection failed: ${upgrade.response.status}")
    }
  }

  connected onComplete println

  done onComplete println

  sourceActor ! TextMessage("Rob")
}
