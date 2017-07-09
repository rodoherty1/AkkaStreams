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


class FlowExampleSpec extends FlatSpec with FlowFixture with BeforeAndAfterAll {

  override def afterAll(): Unit = system.terminate()

  implicit val timeout = Timeout(5.seconds)

  "Flow.map" should "whatever" in {
    val actorRef : ActorRef = system.actorOf(Props(classOf[MyActor]))

    type Input = String
    type Output = String

    val flow: Flow[Input, Output, NotUsed] = Flow[Input].map(_ => "Howdy")

    val graph = Source.single("Beep") via flow to Sink.foreach(println)

    graph.run()(mat)
  }


  "Flow.mapAsync" should "whatever" in {
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

trait FlowFixture extends StreamsFixture {
}

class MyActor extends Actor {
  override def receive: Receive = {
    case x: String => sender() ! "Howdy"
  }
}
