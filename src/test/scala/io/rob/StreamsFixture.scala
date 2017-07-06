package io.rob

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by rodoh on 06/07/2017.
  */
trait StreamsFixture {
  implicit val system = ActorSystem()
  val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
}
