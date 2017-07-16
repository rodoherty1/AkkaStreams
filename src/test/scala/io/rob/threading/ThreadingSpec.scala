package io.rob.threading

import akka.NotUsed
import akka.stream.scaladsl.{Keep, Sink, SinkQueueWithCancel, Source}
import io.rob.StreamsFixture
import org.scalatest._

import scala.concurrent.Future
import scala.util.Success

/**
  * http://akka.io/blog/2016/07/06/threading-and-concurrency-in-akka-streams-explained
  *
  * Streams do not run on the caller thread.
  * Instead, they run on a different thread in the background, without blocking the caller.
  */
class ThreadingSpec extends AsyncFlatSpec with StreamsFixture with Matchers with BeforeAndAfterAll {
  behavior of "Akka Streams"

  override def afterAll(): Unit = system.terminate()

  it should "not run a graph on the caller's thread" in {
    val callersThread = Thread.currentThread().getName

    val source = Source.single("Hello World")
    val sink = Sink.queue[String]()

    val queue: SinkQueueWithCancel[String] = source
      .map(_ => Thread.currentThread().getName)
      .toMat(sink)(Keep.right)
      .run()

    queue.pull() collect[Assertion] {
      case Some(akkaThread) => akkaThread should not be callersThread
    }
  }

  it should "reuse the same thread where possible but suspend threads when required" in {
    val callersThread = Thread.currentThread().getName

    def from(n: Int): Stream[Int] = n #:: from(n + 1)

    val source = Source.fromIterator[Int](() => from(0).iterator)

    val sink = Sink.fold[Map[String, Int], (Int, String)](Map.empty[String, Int]) {
      case (map, (i, threadName)) =>
        // println(s"$i, $threadName")
        val threadOccurrences = map.getOrElse(threadName, 1)
        map updated (threadName, threadOccurrences)
    }

    val akkaThreads = source
        .take(1000000)
        .map(i => (i, Thread.currentThread().getName))
        .runWith(sink)

    akkaThreads map { threads =>
      println(threads)
      threads.size should be > 1
    }
  }
}


