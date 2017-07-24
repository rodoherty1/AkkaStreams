package io.rob

import akka.{Done, NotUsed}
import akka.actor.Cancellable
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import org.scalatest._

import scala.collection.immutable.Queue
import scala.concurrent.Future

class BrownBag2Spec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  "Sink's materialized value" should "successfully complete" in {
    val source: Source[String, NotUsed] = Source.repeat("Howdy").take(10)

    val sink: Sink[String, Future[Done]] = Sink.foreach[String](println)

    val graph: RunnableGraph[Future[Done]] = source.toMat(sink)(Keep.right)

    graph.run()(mat) map { _ shouldBe Done }
  }

  "Source.tick" should "produce some ticks and then be cancelled"  in {
    import scala.concurrent.duration._
    val source: Source[String, Cancellable] = Source.tick(0.seconds, 500.millis, "Tick")

    val sink: Sink[String, Future[Queue[String]]] =
      Sink.fold[Queue[String], String](Queue.empty[String])((q, element) => {
        println(element)
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
