package io.rob

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}

import scala.io.StdIn

object MyWebsocketsServer extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  def greeter(): Flow[Message, Message, Any] = {
    Flow.fromGraph(GraphDSL.create() { builder => {
      val flow: Flow[Message, TextMessage, NotUsed] = Flow[Message] flatMapConcat {
        case tm: TextMessage =>
          tm.textStream map {s => TextMessage(s"Hello $s !")}

        case bm: BinaryMessage =>
          Source.empty
      }

      builder.add(flow)

      flow.shape
    }})
  }


  val route: Route =
    path("hello") {
      get {
        handleWebSocketMessages(greeter())
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}