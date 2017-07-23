package io.rob.websockets.helloworld

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, FlowShape, Graph}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Sink, Source}

import spray.json._

import scala.io.StdIn

case class FeedsMessage (sport: String, incident: String)

object MyJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val feedsMessageFormat: RootJsonFormat[FeedsMessage] = jsonFormat2(FeedsMessage.apply)
}

object HelloWorldWebsocketsServer extends App {

  import MyJsonProtocol._

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val flow: Graph[FlowShape[Message, TextMessage.Strict], Any] = GraphDSL.create(s1) { implicit builder =>flow =>
    import GraphDSL.Implicits._

    val incidentProcessor = Sink foreach println

    val broadcast = builder.add(Broadcast[FeedsMessage](2))

    val ack = builder.add(Flow[FeedsMessage] map { _ => TextMessage.Strict("ACK")})

    flow ~> broadcast.in

    broadcast.out(0) ~> incidentProcessor
    broadcast.out(1) ~> ack.in

    FlowShape(flow.in, ack.out)
  }

  def s1: Flow[Message, FeedsMessage, NotUsed] = {
    import spray.json._
    Flow[Message] flatMapConcat {
      case tm: TextMessage => tm.textStream map { s => s.parseJson.convertTo[FeedsMessage]}

      case bm: BinaryMessage =>
        Source.empty
    }
  }

  val route: Route =
    path("incident") {
      get {
        handleWebSocketMessages(Flow.fromGraph(flow))
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8083)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}