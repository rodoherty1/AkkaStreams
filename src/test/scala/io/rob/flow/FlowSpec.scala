package io.rob.flow

import akka.NotUsed
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import io.rob.StreamsFixture
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

import scala.concurrent.Future
import scala.concurrent.duration._


class FlowSpec extends FlatSpec with StreamsFixture with BeforeAndAfterAll {

  type Input = String
  type Output = String

  override def afterAll(): Unit = system.terminate()

  implicit val timeout = Timeout(5.seconds)

  "Flow.fromFunction and map" should "be composed together to create new flows" in {
    val actorRef : ActorRef = system.actorOf(Props(classOf[MyActor]))

    val f1: Flow[Input, Output, NotUsed] = Flow.fromFunction((s: Input) => "Howdy")

    val f2: Flow[Input, Int, NotUsed] = f1.map(s => s.length)

    val graph = Source.single("Beep") via f1 via f2 to Sink.foreach(println)

    graph.run()(mat)
  }


  "Flow.mapAsync" should "demonstrate how to use my own ActorRef as part of a flow" in {
    val actorRef : ActorRef = system.actorOf(Props(classOf[MyActor]))

    type Input = String
    type Output = String

    val queryActor : Input => Future[Output] = (actorRef ? _) andThen (_.mapTo[Output])

    val actorQueryFlow: Int => Flow[Input, Output, NotUsed] =
      (parallelism: Int) => Flow[Input].mapAsync[Output](parallelism)(queryActor)

    val flow: Flow[Input, Output, NotUsed] = actorQueryFlow(1)

    val graph = Source.single("Beep") via flow to Sink.foreach(println)

    graph.run()(mat)
  }
}

class MyActor extends Actor {
  override def receive: Receive = {
    case x: String => sender() ! "Howdy"
  }
}
