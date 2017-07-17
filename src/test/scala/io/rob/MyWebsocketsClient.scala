package io.rob

import akka.actor.ActorRef
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, WebSocketRequest, WebSocketUpgradeResponse}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

object MyWebsocketsClient extends App {

  // Continue work on this - http://doc.akka.io/docs/akka-http/10.0.9/scala/http/client-side/websocket-support.html

  private def websocketRequest(hostname: String, port: Int): (Future[WebSocketUpgradeResponse], ActorRef) = {
    Http().singleWebSocketRequest(WebSocketRequest(s"ws://localhost:8080/hello"), flow)
  }

  private lazy val flow: Flow[Message, Message, ActorRef] = Flow.fromSinkAndSourceMat(
    Sink.actorRef[Message](wsConsumer.ref, 'wsClosed), Source.actorRef[Message](1000, OverflowStrategy.dropTail)
  )((_, ref) => ref)



}
