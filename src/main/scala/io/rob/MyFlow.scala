package io.rob

import akka.NotUsed
import akka.stream.scaladsl.Flow

object MyFlow {

  val simpleFlow: Flow[Int, String, NotUsed] = Flow[Int].map(_.toString)


}
