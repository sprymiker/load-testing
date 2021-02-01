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

trait GuestNonPersistentCartCheckoutApiBase {

   lazy val scenarioName = "API: Guest Non-persistent Cart Checkout"

  val httpProtocol = GlueProtocol.httpProtocol
  val targetIterations = if (Scenario.iterations > 69 ) 70 else 2
  val feeder = tsv(s"resources/scenarios/payload/non_persistent_cart_items_x${targetIterations}.csv").circular;
  val customerUniqueIdFeeder: Iterator[Map[String, String]] = Iterator.continually(Map("customerUniqueId" -> (Random.alphanumeric.take(25).mkString)))

  val request = http("Checkout Guest cart")
    .post("/checkout")
    .body(StringBody("""{"data": {"type": "checkout", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "quote": {"currency": {"code": "EUR"}, "priceMode": "GROSS_MODE", "items": ${items} }, "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${customerUniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(customerUniqueIdFeeder)
    .feed(feeder)
    .exec(request)
}

class GuestNonPersistentCartCheckoutApiRamp extends Simulation with GuestNonPersistentCartCheckoutApiBase {
  override lazy val scenarioName = "API: Guest Non-persistent Cart Checkout [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class GuestNonPersistentCartCheckoutApiSteady extends Simulation with GuestNonPersistentCartCheckoutApiBase {
  override lazy val scenarioName = "API: Guest Non-persistent Cart Checkout [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
