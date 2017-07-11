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

### Graphs
* Broadcast **_Todo_**
* Merge **_Todo_**

### Websocket Client with a Websocket Server
* Have the client call the server and make the server return a result **_Todo_**

## Links
* [Akka Stream docs] (http://doc.akka.io/docs/akka/current/scala/stream/stream-integrations.html)
* [Actors in an Akka Stream] (https://stackoverflow.com/questions/39125760/creating-a-flow-from-actor-in-akka-streams)

## Topics that are not covered in this session
* Supervision strategies
* Streams insides Streams (e.g. an ActorRef that kicks off its own Stream)
* Distributed Streams
* Backpressure in depth

## Todo
* Move some of examples from ```src/main/scala``` to ```src/test/scala```
* Document the basic terminology (graph stages, processing stage)
* Read this - http://blog.colinbreck.com/integrating-akka-streams-and-akka-actors-part-ii/
* Study this video and try to create a json parser - https://www.youtube.com/watch?v=x62K4ObBtw4&t=1692s


