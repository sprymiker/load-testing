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
import spryker.GlueFrontProtocol._
import spryker.Scenario._

trait CatalogSearchApiBase {

  lazy val scenarioName = "Catalog Search Api"

  val httpProtocol = GlueFrontProtocol.httpProtocol
  val feeder = csv("tests/_data/product_groups.csv").random

  val request = http(scenarioName)
    .get("/catalog-search")
    .queryParam("q", "${group}")
    .queryParam("include", "abstract-products,concrete-products")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(request)
}

class CatalogSearchApiRamp extends Simulation with CatalogSearchApiBase {

  override lazy val scenarioName = "Catalog Search API [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class CatalogSearchApiSteady extends Simulation with CatalogSearchApiBase {

  override lazy val scenarioName = "Catalog Search API [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
