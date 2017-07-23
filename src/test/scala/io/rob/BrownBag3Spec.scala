package io.rob

import akka.NotUsed
import akka.actor.Cancellable
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import org.scalatest._

import scala.collection.immutable.Queue
import scala.concurrent.Future

/**
  * Created by rodoh on 06/07/2017.
  */
class BrownBag3Spec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  "Source.repeat" should "repeat an element" in {
    val source = Source.repeat("Howdy").take(10)

    val sink = Sink.foreach(println)

    val graph = source to sink

    graph.run() shouldBe NotUsed
  }

  "Source.tick" should "produce some ticks and then be cancelled"  in {
    import scala.concurrent.duration._
    val source: Source[String, Cancellable] = Source.tick(0.seconds, 500.millis, "Tick")

    val sink: Sink[String, Future[Queue[String]]] =
      Sink.fold[Queue[String], String](Queue.empty[String])((q, element) => {
        q.enqueue(element)
      })

    val graph: RunnableGraph[(Cancellable, Future[Queue[String]])] = source.toMat(sink)(Keep.both)

    val (cancellable, queue) = graph.run()(mat)

    Thread.sleep(2.seconds.toMillis)

    cancellable.cancel()

    cancellable shouldBe 'cancelled

    queue map { q =>
      q should contain atLeastOneElementOf List("Tick")
    }
  }
}
