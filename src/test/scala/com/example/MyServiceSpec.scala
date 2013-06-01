package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MyServiceSpec extends Specification with Specs2RouteTest with MyService {
  def actorRefFactory = system
  
  "MyService" should {

    "return a not found for the root path" in {
      Get() ~> testRoute ~> check {
        handled must beFalse
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/test") ~> testRoute ~> check {
        entityAs[String] must contain(""""name": "abc"""")
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/test") ~> sealRoute(testRoute) ~> check {
        status === MethodNotAllowed
        entityAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}