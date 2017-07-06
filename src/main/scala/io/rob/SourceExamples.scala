package io.rob

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

object SourceExamples extends App {

  implicit val system = ActorSystem()
  private val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  val graph = Source.single("Hello").toMat(Sink.foreach(println))(Keep.right)

  graph.run()(mat) onComplete { _ =>
    system.terminate()
  }
}
