package io.rob.sink

import java.nio.file.Paths

import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString
import io.rob.StreamsFixture
import org.scalatest._

import scala.collection.immutable.Queue
import scala.concurrent.Future


class SinkExamplesSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  "A Sink backed by a Queue" should "store the numbers produed by the Source" in {
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

  "A sink backed by a File" should """write "Hello World" to file""" in {
    val file = Paths.get("hello.txt")
    file.toFile.deleteOnExit()

    val source = Source.single("Hello World")

    val flow = Flow.fromFunction[String, ByteString](ByteString.apply)

    val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(file)

    val graph: RunnableGraph[Future[IOResult]] = (source via flow).toMat(sink)(Keep.right)

    graph.run()(mat) map { _.wasSuccessful shouldBe true }
  }
}
