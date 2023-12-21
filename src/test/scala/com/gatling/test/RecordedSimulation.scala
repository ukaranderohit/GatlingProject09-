package com.gatling.test

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("https://petstore.octoperf.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")

val feeder = csv("data/credentials.csv").circular

	val scn = scenario("RecordedSimulation")
		.feed(feeder)
		.exec(http("OpenJpetStore")
			.get("/actions/Catalog.action"))
		.pause(1)

		.exec(http("ClickSignOn")
			.get("/actions/Account.action?signonForm="))
		.pause(8)

		.exec(http("ProvideDetails_Login")
			.post("/actions/Account.action")
			.formParam("username", "#{username}")
			.formParam("password", "#{password}")
			.formParam("signon", "Login")
			.formParam("_sourcePage", "NNJFR1-aliX5ZS-h3f1eiyuFJYc2Zij7IUUq0QqlPfIkva3e5ZKGwiZOeII8HDWUqJZUwBAplaD62k6xOKRq2ln5yiW-5kAC4Rpc4t1OEp4=")
			.formParam("__fp", "rQ4bv_cCZwbGVroVYQwfr8xn3OhaW7IhboVDG7FTXmeWLA13T9okzWllb2udUCh4"))
		.pause(1)


		.exec(http("Logout")
			.get("/actions/Account.action?signoff="))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}