package io.rob

import akka.actor.ActorRef
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source

import scala.concurrent.Promise

object MySource {

  val sourceActorRef: Source[Int, ActorRef] = Source.actorRef[Int](10, OverflowStrategy.dropHead)

  val simpleSource: Source[Int, Promise[Option[Int]]] = Source.maybe[Int]

}
