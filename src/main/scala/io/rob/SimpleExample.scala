package io.rob

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.util.Success

object SimpleExample extends App {

  import MySink._
  import MyFlow._
  import MySource._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val source = simpleSource.via(simpleFlow)

  val (src, snk)  = source.toMat(simpleSink)(Keep.both).run

  snk.pull().onComplete {
    case Success(Some(v)) => println(s"Success = $v")
    case _ => println("Failure!")
  }

  src.completeWith {
    Future(
      Some(99)
    )
  }
}
