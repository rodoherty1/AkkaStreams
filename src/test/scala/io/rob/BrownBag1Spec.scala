package io.rob

import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import akka.{Done, NotUsed}
import org.scalatest._

import scala.concurrent.{Future, Promise}

class BrownBag1Spec extends FlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  // Demo 1 - The Absolute Basics
  "Source.single" should """say "hello world"""" in {
    val source = Source.single("Hello World")

    val sink = Sink.foreach(println)

    val graph = source to sink

    graph.run()
  }

  // Demo 2 - Flows
  "Flow.fromFunction and map" should "be composed together to create new flows" in {
    val f1: Flow[String, String, NotUsed] = Flow.fromFunction((s: String) => s.reverse)

    val f2: Flow[String, Int, NotUsed] = f1.map(s => s.length)

    val flow = f1 via f2

    val graph = Source.single("Beep") via flow to Sink.foreach(println)

    graph.run()
  }


  // Demo 3 - Introducing the Materializer and Materializd Values
  "Source.maybe" should """say "hello world" again""" in {
    val source: Source[String, Promise[Option[String]]] = Source.maybe[String]
    // val source2: Source[String, ActorRef] = Source.actorRef[String](5, OverflowStrategy.dropHead)

    val sink: Sink[String, Future[Done]] = Sink.foreach[String](println)

    val graph: RunnableGraph[Promise[Option[String]]] = source.toMat(sink)(Keep.left)

    val materializedValue: Promise[Option[String]] = graph.run()(mat)

    materializedValue.completeWith {
      Future {
        Some("Hello World)")
      }
    }
  }


}
