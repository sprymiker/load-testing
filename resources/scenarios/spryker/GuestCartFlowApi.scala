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
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import java.lang.System.getProperty
import scala.concurrent.duration.{Duration, MILLISECONDS}
import scala.util.Random

trait GuestCartFlowApiBase {
  lazy val scenarioName = "API: Guest Cart Flow"

  val httpProtocol: HttpProtocolBuilder = GlueProtocol.httpProtocol
  val glueFrontBaseUrl = GlueFrontProtocol.baseUrl.stripSuffix("/")

  val customerUniqueIdFeeder: Iterator[Map[String, String]] = Iterator.continually(Map("customerUniqueId" -> (Random.alphanumeric.take(25).mkString)))
  val quantityFeeder = Iterator.continually(Map("quantity" -> 1))
  val productGroups = csv("tests/_data/product_groups.csv").readRecords

  val searchRequest: HttpRequestBuilder = http("Catalog Search for a random product")
    .get(glueFrontBaseUrl + "/catalog-search")
    .queryParam("q", "${group}")
    .queryParam("include", "abstract-products")
    .check(status.is(200))
    .check(
      jsonPath("$.included[*].attributes.attributeMap.product_concrete_ids[0]")
        .findRandom
        .saveAs("productId")
    )

  val addToCartRequest: HttpRequestBuilder = http("Add each product to Guest Cart")
    .post("/guest-cart-items")
    .body(StringBody("""{"data": {"type": "guest-cart-items", "attributes": {"sku": "${productId}", "quantity": "${quantity}"}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${customerUniqueId}")
    .header("Content-Type", "application/json")
    .check(status.saveAs("statusCode"))
    .check(status.is(201))
    .check(jsonPath("$.data.id").saveAs("cartUuid"))

  val checkoutDataRequest: HttpRequestBuilder = http("Checkout data Guest cart")
    .post("/checkout-data")
    .body(StringBody("""{"data": {"type": "checkout-data", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "idCart": "${cartUuid}", "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${customerUniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(200))

  val checkoutRequest: HttpRequestBuilder = http("Checkout Guest cart")
    .post("/checkout")
    .body(StringBody("""{"data": {"type": "checkout", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "idCart": "${cartUuid}", "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${customerUniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(customerUniqueIdFeeder)
    .exec { session: Session =>
      session.set("productGroups", Random.shuffle(productGroups).slice(0, Scenario.iterations.toInt))
    }
    .foreach(session => session("productGroups").as[Seq[Map[String, Any]]], "productGroup") {
      exec(flattenMapIntoAttributes("${productGroup}"))
      .feed(quantityFeeder)
      .exec(searchRequest)
      .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
      .exec(addToCartRequest)
      .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
    }
    .exec(checkoutDataRequest)
    .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
    .exec(checkoutRequest)
}

class GuestCartFlowApiRamp extends Simulation with GuestCartFlowApiBase {
  override lazy val scenarioName = "API: Guest Cart Flow [Incremental]"

  setUp(scn.inject(
    rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration)
  )).protocols(httpProtocol)
}

class GuestCartFlowApiSteady extends Simulation with GuestCartFlowApiBase {
  override lazy val scenarioName = "API: Guest Cart Flow [Steady RPS]"

  setUp(scn.inject(
    constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration)
  )).protocols(httpProtocol)
}
