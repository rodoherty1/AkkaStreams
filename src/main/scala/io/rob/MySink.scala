package io.rob

import akka.stream.scaladsl.{Sink, SinkQueueWithCancel}

object MySink {
  val simpleSink: Sink[String, SinkQueueWithCancel[String]] = Sink.queue[String]()

}
