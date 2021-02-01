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

trait GuestNonPersistentCartCalculateApiBase {

   lazy val scenarioName = "API: Guest Non-persistent Cart Calculate"

  val httpProtocol = GlueProtocol.httpProtocol
  val targetIterations = if (Scenario.iterations > 69 ) 70 else 2
  val feeder = tsv(s"resources/scenarios/payload/non_persistent_cart_items_x${targetIterations}.csv").circular;

  val request = http("Calculate Guest Cart")
    .post("/carts-calculation")
    .body(StringBody("""{"data": {"type": "carts-calculation", "attributes": {"store": "DE", "currency": "EUR", "priceMode": "GROSS_MODE", "items": ${items}}}}""")).asJson
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(request)
}

class GuestNonPersistentCartCalculateApiRamp extends Simulation with GuestNonPersistentCartCalculateApiBase {
  override lazy val scenarioName = "API: Guest Non-persistent Cart Calculate [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class GuestNonPersistentCartCalculateApiSteady extends Simulation with GuestNonPersistentCartCalculateApiBase {
  override lazy val scenarioName = "API: Guest Non-persistent Cart Calculate [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
