package io.rob

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.util.Success

object SourceActorExample extends App {

  import MySink._
  import MyFlow._
  import MySource._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val src = sourceActorRef via simpleFlow

  val (source, snk)  = src.toMat(simpleSink)(Keep.both).run

  snk.pull().onComplete {
    case Success(Some(v)) => println(s"Success = $v")
    case _ => println("Failure!")
  }

  source ! 99
}
