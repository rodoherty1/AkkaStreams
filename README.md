# AkkaStreams [![Build Status](https://travis-ci.org/rodoherty1/AkkaStreams.svg?branch=adding-travis-stuff)](https://travis-ci.org/rodoherty1/AkkaStreams)

## Basic Examples in Unit Tests
* Sources, Folds and Sinks
* Materialized Values from Sources and Sinks
* Tickers
* Simple Websocket Client and Server

### Threading in Unit Tests
* Demonstrate that an Akka Stream is not called on the caller's thread
* Demomstrate that AkkaStreams will generally reuse a thread when executing successive processing stages
* Demomstrate that AkkaStreams will occasionally suspend a thread and use a different thread from the threadpool.

### Akka-Http
* Akka-Http was intended to be a primary use case for AkkaStreams
* Akka-Http provide back pressure all the way back to the TCP layer

## Links
* [Akka Stream docs] (http://doc.akka.io/docs/akka/current/scala/stream/stream-integrations.html)
* [Akka Streams Terminology] (http://doc.akka.io/docs/akka/2.5.2/java/stream/stream-flows-and-basics.html)
* [Integrating Akka Streams and Actors - Colin Breck] (http://blog.colinbreck.com/integrating-akka-streams-and-akka-actors-part-i)
* [Actors in an Akka Stream] (https://stackoverflow.com/questions/39125760/creating-a-flow-from-actor-in-akka-streams)
* [Threading in AkkaStreams] (http://akka.io/blog/2016/07/06/threading-and-concurrency-in-akka-streams-explained)
* [Heiko Seeberger's talk on AkkaHttp and Graphs] (https://www.youtube.com/watch?v=ryxrWVI3PMA&t=1191s)
* [Graph Stages 1] (http://akka.io/blog/2016/07/30/mastering-graph-stage-part-1)
* [Graph Stages 2] (http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage)
* [Akka's Akka Stream Announcement] (https://www.lightbend.com/blog/typesafe-announces-akka-streams)

## Topics that are not covered in this session
* Supervision strategies
* Streams insides Streams (e.g. an ActorRef that kicks off its own Stream)
* Distributed Streams
* The Graph DSL and combining Partial Graphs

## FAQ
Q: Should I use many ActorMaterializers?
A: You should not need more than a couple of Materializers and those cases where you either want
   1) to bind the lifecycle of a bunch of streams to an Actor (in which case you create a Materializer with the Actor's context.system so that when the actor dies, all streams under that materializer will be terminated).
   2) if you want to shut down a bunch of streams together by calling materializer.shutdown() which will terminate all streams belonging to that materializer.

## Must haves Todos
* Document the basic terminology (graph stages, processing stage)
* Tidy up the websock server and client example.  Describe the streams on either side and describe how backpressure works.

