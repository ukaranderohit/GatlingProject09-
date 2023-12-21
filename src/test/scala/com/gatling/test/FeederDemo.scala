package com.gatling.test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FeederDemo extends Simulation{

//  Protocol
val httpProtocol=http.baseUrl("https://computer-database.gatling.io/")


//  scenario

  val feeder = csv("data/testusers.csv").circular

  val scn = scenario("Feeder Demo")
    .repeat(5){
      feed(feeder)
        .exec{session=>
          println("Name: " + session("name").as[String])
          println("Job: " + session("job").as[String])
          println("Pages: " + session("page").as[String])
          session
        }
    }
    .pause(2)
    .exec(
      http("Goto ${page}")
        .get("/#{page}")
    )


  setUp(scn.inject(atOnceUsers(3))).protocols(httpProtocol)
}
