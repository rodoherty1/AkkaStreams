package io.rob

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl._

import scala.util.Success

object SimpleGraphExample extends App {

  import MyFlow._
  import MySink._
  import MySource._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val runnableGraph = RunnableGraph.fromGraph(GraphDSL.create(sourceActorRef, simpleFlow, simpleSink)((_, _, _)) { implicit builder => (src, flow, snk) =>
    import GraphDSL.Implicits._
    src ~> flow ~> snk
    ClosedShape
  })

  val (src, _, snk) = runnableGraph.run()

  snk.pull().onComplete {
    case Success(Some(v)) => println(s"Success = $v")
    case _ => println("Failure!")
  }

  src ! 99
}
