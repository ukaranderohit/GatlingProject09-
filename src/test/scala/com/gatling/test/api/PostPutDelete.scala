package com.gatling.test.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostPutDelete extends Simulation{

  val httpProtocol = http
    .baseUrl("https://reqres.in/api")

  val createuser = scenario("Create User Request")
    .exec(
      http("Create User")
        .post("/users")
        .body(RawFileBody("data/users.json")).asJson
//        .body(StringBody(
//          """
//            |{
//            |    "name": "morpheus",
//            |    "job": "leader"
//            |}
//            |""".stripMargin)).asJson
        .check(
          status is 201,
          jsonPath("$.name") is "morpheus"

        )
    )
    .pause(1)

  val updateuser = scenario("Update User Request")
    .exec(
      http("Update User Request")
        .put("/users/2")
        .body(RawFileBody("data/users.json")).asJson
        .check(
          status is 200,
          jsonPath("$.name") is "morpheus"
        )

    )

  setUp(
    createuser.inject(rampUsers(10).during(10)),
    updateuser.inject(rampUsers(2).during(10))
  ).protocols(httpProtocol)
}
