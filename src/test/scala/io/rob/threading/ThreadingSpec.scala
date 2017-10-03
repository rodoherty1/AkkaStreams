package io.rob.threading

import akka.NotUsed
import akka.stream.scaladsl.{Keep, Sink, SinkQueueWithCancel, Source}
import io.rob.StreamsFixture
import org.scalatest._

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

  ignore should "reuse the same thread where possible but suspend threads when required" in {
    val callersThread = Thread.currentThread().getName

    def from(n: Int): Stream[Int] = n #:: from(n + 1)

    val source: Source[Int, NotUsed] = Source.fromIterator[Int](() => from(0).iterator)

    def zipWithThreadName(i: Int): (Int, String) = (i, Thread.currentThread().getName)

    val sink = Sink.fold[Map[String, Int], (Int, String)](Map.empty[String, Int]) {
      case (map, (i, threadName)) =>
        val threadOccurrences = map.getOrElse(threadName, 0)
        map updated (threadName, threadOccurrences + 1)
    }

    val threadnamesUsed = source
        .take(10000)
        .map(zipWithThreadName)
        //. map{ case (i, threadName) => println(s"$i, $threadName"); (i, threadName)}  // Uncomment to debug
        .runWith(sink)

    threadnamesUsed map { threadnames =>
      printout(threadnames)
      threadnames.size should be > 1
    }
  }

  def printout(threadnames: Map[String, Int]): Unit = {
    println ("Count    Threadname")
    println ("=====    ==========")
    threadnames.foreach {
      case (name, count) => println(s"$count    $name")
    }
  }

}


