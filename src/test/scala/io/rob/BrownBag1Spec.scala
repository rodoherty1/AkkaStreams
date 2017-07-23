package io.rob

import akka.Done
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import org.scalatest._

import scala.concurrent.{Future, Promise}

/**
  * Created by rodoh on 06/07/2017.
  */
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

  // Demo 2 - Introducing the Materializer and Materializd Values
  "Source.maybe" should """say "hello world" again""" in {
    val source: Source[String, Promise[Option[String]]] = Source.maybe[String]

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
