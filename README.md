# AkkaStreams

## Examples
### Sources
* Source(1 to 10) print to screen
* Source from disk
* Source from website
* Source from a plain old function like List.unfold

### Flows
* A flow that uses my own ActorRef

### Sinks
* Sink that buffers elements into a collection
* Sink that writes to disk
* Sink that submits to a http request

### Threading
* Demonstrate that an Akka Stream is not called on the caller's thread
* Demomstrate that AkkaStreams will generally reuse a thread when executing successive processing stages
* Demomstrate that AkkaStreams will occasionally suspend a thread and use a different thread from the threadpool.

### Graphs
* Read http://akka.io/blog/2016/07/30/mastering-graph-stage-part-1 and http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage ** TODO **
* Broadcast **_Todo_**
* Merge **_Todo_**

### Akka-Http
* Akka-Http was intended to be a primary use case for AkkaStreams
* Akka-Http provide back pressure all the way back to the TCP layer
* Recall Ktoso's example of a curl request which receives tweets and is then suddenly suspended.
*
* Simple HelloWorld Websockets Client and Server
** TODO ** Need to be able to describe the flows on either side and what happens when either disconnects.  How does back-pressure work when you have a fast client and slow server.
** TODO ** Maybe I could use this in a slightly more elaborate server.  http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage

## Must haves Todos
* Document the basic terminology (graph stages, processing stage)
* Look for examples in our code of where we use Streams, explain why we chose Streams and explain how Streams might help us in the future.
* Explain how Akka Streams is memory bounded - i.e. bounded queues with overflow strategies and backpressure.
* Tidy up the websock server anf client example.  Describe the streams on either side and describe how backpressure works.

## Nice to Have Todos
* Read this - http://blog.colinbreck.com/integrating-akka-streams-and-akka-actors-part-ii/
* Move some of examples from ```src/main/scala``` to ```src/test/scala```
* Remember that not everyone is a Scala coder - Keep examples syntactically simple.
* Read http://akka.io/blog/2016/07/30/mastering-graph-stage-part-1 and http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage

## Links
* [Akka Stream docs] (http://doc.akka.io/docs/akka/current/scala/stream/stream-integrations.html)
* [Actors in an Akka Stream] (https://stackoverflow.com/questions/39125760/creating-a-flow-from-actor-in-akka-streams)
* [Threading in AkkaStreams] (http://akka.io/blog/2016/07/06/threading-and-concurrency-in-akka-streams-explained)
* [Heiko Seeberger's talk on AkkaHttp and Graphs] (https://www.youtube.com/watch?v=ryxrWVI3PMA&t=1191s)
* [Websocket Server and

## Topics that are not covered in this session
* Supervision strategies
* Streams insides Streams (e.g. an ActorRef that kicks off its own Stream)
* Distributed Streams
* Backpressure in depth


