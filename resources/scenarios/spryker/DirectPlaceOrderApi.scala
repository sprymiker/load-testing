/*
 * Copyright 2011-2019 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random
import spryker.GlueProtocol._
import spryker.Scenario._

trait DirectPlaceOrderBaseApi {

  lazy val scenarioName = "API: Direct PlaceOrder"

  val httpProtocol = GlueProtocol.httpProtocol
  val targetIterations = if (Scenario.iterations > 69 ) 70 else 2
  val feeder = tsv(s"resources/scenarios/payload/place_order_payload_x${targetIterations}.csv").circular;

  val request = http(scenarioName)
    .post("/place-order-test")
    .body(StringBody("""{"data": {"type": "place-order-test", "attributes": {"payload": ${payload}}}}""")).asJson
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(request)
}

class DirectPlaceOrderApiRamp extends Simulation with DirectPlaceOrderBaseApi {

  override lazy val scenarioName = "API: Direct PlaceOrder [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class DirectPlaceOrderApiSteady extends Simulation with DirectPlaceOrderBaseApi {

  override lazy val scenarioName = "API: Direct PlaceOrder [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
