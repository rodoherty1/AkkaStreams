package io.rob

import akka.stream.scaladsl.{Sink, Source}
import org.scalatest.FlatSpec

class ThreadingSpec extends FlatSpec with StreamsFixture  {
  behavior of "Akka Streams threading"

  it should "" in {
    println(Thread.currentThread().getName)

    Source.single("Hello")
      .map(_ + " Stream World!")
      .to(Sink.foreach(s ⇒ println(s"${Thread.currentThread().getName} $s")))
      .run()(mat)

    println("running")
    Thread.sleep(1000) // Wait a bit before we shut down the system

    system.terminate()
  }

}
