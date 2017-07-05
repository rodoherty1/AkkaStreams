package io.rob

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by rodoh on 05/07/2017.
  */
object Test1 extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val src = Source.tick(0.seconds, 1.seconds, "Hello")

  val toUpper = Flow.fromFunction[String, String](_.toUpperCase)

  val sink = Sink.foreach(println)

  (src via toUpper to sink).run
}
