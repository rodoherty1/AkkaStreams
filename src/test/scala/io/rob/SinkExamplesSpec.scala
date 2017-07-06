package io.rob

import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import org.scalatest._

import scala.collection.immutable.Queue
import scala.concurrent.Future


class SinkExamplesSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  "A sink" should "verify that the numbers 1 to 10 were produced" in {
    val source = Source(1 to 10)

    val sink: Sink[Int, Future[Queue[Int]]] =
      Sink.fold[Queue[Int], Int](Queue.empty[Int])((q, element) => {
        q.enqueue(element)
      })

    val graph: RunnableGraph[Future[Queue[Int]]] = source.toMat(sink)(Keep.right)

    graph.run()(mat) map { queue =>
      queue should contain theSameElementsAs (1 to 10)
    }
  }
}
