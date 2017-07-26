package io.rob

import akka.NotUsed
import akka.stream.scaladsl.{Keep, Sink, SinkQueueWithCancel, Source}
import org.scalatest._

import scala.concurrent.Future

/**
  * http://akka.io/blog/2016/07/06/threading-and-concurrency-in-akka-streams-explained
  *
  * Streams do not run on the caller's thread.
  * Instead, they run on a different thread in the background, without blocking the caller.
  */
class BrownBag3Spec extends AsyncFlatSpec with StreamsFixture with Matchers with BeforeAndAfterAll {
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

    val source: Source[Int, NotUsed] = Source.fromIterator[Int](() => from(0).iterator)

    val sink = Sink.fold[Map[String, Int], (Int, String)](Map.empty[String, Int]) {
      case (map, (i, threadName)) =>
        val threadOccurrences = map.getOrElse(threadName, 0)
        map updated (threadName, threadOccurrences + 1)
    }

    val threadnamesUsed: Future[Map[String, Int]] = source
        .take(100000)
        .map { i => (i, Thread.currentThread().getName)}
        .mapConcat{
          case (i, threadName) => List((i, threadName), (i, Thread.currentThread().getName))
        }
        //. map{ case (i, threadName) => println(s"$i, $threadName"); (i, threadName)}  // Uncomment to debug
        .runWith(sink) // runWith is an alternative to "source toMat sink" followed by "run"

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


