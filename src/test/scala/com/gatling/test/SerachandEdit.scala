package com.gatling.test

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class SerachandEdit extends Simulation {

	val httpProtocol = http
		.baseUrl("https://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")

	val search = exec(http("OpenComputerDatabaseHomePage")
		.get("/computers"))
		.pause(2)
		.exec(http("SearchComputer")
			.get("/computers?f=ACE"))
		.pause(2)

	val edit = exec(http("selectComputerAndOpen")
		.get("/computers/381"))
		.pause(2)
		.exec(http("UpdateAndSave")
			.post("/computers/381")
			.formParam("name", "ACE")
			.formParam("introduced", "")
			.formParam("discontinued", "")
			.formParam("company", "2"))
		.pause(2)


	val users = scenario("Users").exec(search)

	val admin = scenario("Admin").exec(search, edit)


	setUp(
		  users.inject(rampUsers(10).during(10)),
			admin.inject(rampUsers(4).during(10))
	).protocols(httpProtocol)


//	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}