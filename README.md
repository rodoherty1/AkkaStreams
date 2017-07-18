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
** TODO ** * Read http://akka.io/blog/2016/07/30/mastering-graph-stage-part-1 and http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage
* Broadcast **_Todo_**
* Merge **_Todo_**

### Websocket Client with a Websocket Server
* Simple HellowWorld Websockets Client and Server
** TODO ** Need to be able to describe the flows on either side and what happens when either disconnects.  How does back-pressure work when you have a fast client and slow server.
** TODO ** Maybe I could use this in a slightly more elaborate server.  http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage

## Links
* [Akka Stream docs] (http://doc.akka.io/docs/akka/current/scala/stream/stream-integrations.html)
* [Actors in an Akka Stream] (https://stackoverflow.com/questions/39125760/creating-a-flow-from-actor-in-akka-streams)
* [Threading in AkkaStreams] (http://akka.io/blog/2016/07/06/threading-and-concurrency-in-akka-streams-explained)
* [Heiko Seeberger's talk on AkkaHttp and Graphs] (https://www.youtube.com/watch?v=ryxrWVI3PMA&t=1191s)

## Topics that are not covered in this session
* Supervision strategies
* Streams insides Streams (e.g. an ActorRef that kicks off its own Stream)
* Distributed Streams
* Backpressure in depth

## Todo
* Move some of examples from ```src/main/scala``` to ```src/test/scala```
* Document the basic terminology (graph stages, processing stage)
* Read this - http://blog.colinbreck.com/integrating-akka-streams-and-akka-actors-part-ii/
* Look for examples in our code of where we use Streams, explain why we chose Streams and explain how Streams might help us in the future.
* Remember that not everyone is a Scala coder - Keep examples syntactically simple.
* Explain how Akka Streams is memory bounded
* Read http://akka.io/blog/2016/07/30/mastering-graph-stage-part-1 and http://akka.io/blog/2016/08/25/simple-sink-source-with-graphstage



