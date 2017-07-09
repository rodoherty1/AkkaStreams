package io.rob.source

import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import akka.{Done, NotUsed}
import io.rob.StreamsFixture
import org.scalatest._

import scala.concurrent.Future

/**
  * Created by rodoh on 06/07/2017.
  */
class SourceSingleExamplesSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with StreamsFixture {

  override def afterAll(): Unit = {
    system.terminate()
  }

  "A source single" should """say "hello world"""" in {
    val source = Source.single("Hello World")
    val sink = Sink.foreach(println)

    val graph = source to sink

    graph.run()(mat) shouldBe NotUsed
  }

  it should """say "hello world" again""" in {
    val source: Source[String, NotUsed] = Source.single[String]("Hello World")

    val sink: Sink[String, Future[Done]] = Sink.foreach[String](println)

    val graph: RunnableGraph[NotUsed] = source.toMat(sink)(Keep.left)

    graph.run()(mat) shouldBe NotUsed
  }

  it should """say "hello world" a third time""" in {
    val source = Source.single("Hello World")

    val sink: Sink[String, Future[Done]] = Sink.foreach[String](println)

    val graph: RunnableGraph[Future[Done]] = source.toMat(sink)(Keep.right)

    graph.run()(mat) map { _ shouldBe Done }
  }
}
