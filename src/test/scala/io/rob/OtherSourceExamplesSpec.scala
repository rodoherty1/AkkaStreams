package io.rob

import java.nio.file.Paths

import akka.actor.Cancellable
import akka.stream.IOResult
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}
import org.scalatest._

import scala.concurrent.Future

class OtherSourceExamplesSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  "Source.repeat" should "repeat an element" in {
    val source = Source.repeat("Howdy").take(10)
    val sink = Sink.foreach(println)

    val graph = source to sink

    graph.run()(mat) shouldBe NotUsed
  }

  "Source.tick" should "produce some ticks and then be cancelled"  in {
    import scala.concurrent.duration._
    val source: Source[String, Cancellable] = Source.tick(0.seconds, 500.millis, "Tick")

    val sink: Sink[String, Future[Done]] = Sink.foreach[String](println)

    val graph: RunnableGraph[Cancellable] = source.toMat(sink)(Keep.left)

    val cancellable = graph.run()(mat)

    Thread.sleep(2.seconds.toMillis)

    cancellable.cancel()

    cancellable shouldBe 'cancelled
  }

}


