package com.gatling.test.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jsonpath.JsonPath
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol

class apitest extends Simulation{

//  Protocol
  val httpProtocol=http
    .baseUrl("https://reqres.in/api/users")



//  Scenaiors

  val scn = scenario("SearchSingleUserDetails")
    .exec(
      http("API Single User")
        .get("/2")
        .asJson
        .check(
          status is 200,
          jsonPath("$.data.first_name") is "Janet"
        )
    )
    .pause(1)

//  SetUp

  setUp(
    scn.inject(rampUsers(5).during(10))
  ).protocols(httpProtocol)
}
