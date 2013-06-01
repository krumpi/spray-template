package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.json._
import MediaTypes._
import spray.httpx.SprayJsonSupport


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(testRoute)
}

case class User(name: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat1(User)
}

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with SprayJsonSupport {
  import JsonProtocol._

  val testRoute =
    path("test") {
      get {
        respondWithMediaType(`application/json`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            User("abc")
          }
        }
      }
    }

}