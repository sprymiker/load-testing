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

trait GuestNonPersistentCartFlowApiBase {
  lazy val scenarioName = "API: Guest Non-persistent Cart Flow"

  val httpProtocol: HttpProtocolBuilder = GlueProtocol.httpProtocol
  val glueFrontBaseUrl = GlueFrontProtocol.baseUrl.stripSuffix("/")

  val customerUniqueIdFeeder: Iterator[Map[String, String]] = Iterator.continually(Map("customerUniqueId" -> (Random.alphanumeric.take(25).mkString)))
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

  val calculateCartRequest: HttpRequestBuilder = http("Add each product to Guest Cart")
    .post("/carts-calculation")
    .body(StringBody("""{"data": {"type": "carts-calculation", "attributes": {"store": "DE", "currency": "EUR", "priceMode": "GROSS_MODE", "items": ${cartItems}}}}""")).asJson
    .header("Content-Type", "application/json")
    .check(status.saveAs("statusCode"))
    .check(status.is(201))

  val checkoutDataRequest: HttpRequestBuilder = http("Checkout data Guest cart")
    .post("/checkout-data")
    .body(StringBody("""{"data": {"type": "checkout-data", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "quote": {"currency": {"code": "EUR"}, "priceMode": "GROSS_MODE", "items": ${cartItems} }, "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${customerUniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(200))

  val checkoutRequest: HttpRequestBuilder = http("Checkout Guest cart")
    .post("/checkout")
    .body(StringBody("""{"data": {"type": "checkout", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "quote": {"currency": {"code": "EUR"}, "priceMode": "GROSS_MODE", "items": ${cartItems} }, "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${customerUniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(customerUniqueIdFeeder)
    .exec { session: Session =>
      session.set("productGroups", Random.shuffle(productGroups).slice(0, Scenario.iterations.toInt))
    }
    .exec { session: Session =>
      session.set("cartItems", "[]")
    }
    .foreach(session => session("productGroups").as[Seq[Map[String, Any]]], "productGroup") {
      exec(flattenMapIntoAttributes("${productGroup}"))
      .exec(searchRequest)
      .exec(session =>
        {
          var cartItems = session("cartItems").as[String];
          var connector = ",";
          if (cartItems == "[]") {
            connector = ""
          }

          println( "+++ " + cartItems);
          val newItem = """{"sku": """" + session("productId").as[String] + """", "quantity": "1"}""";
          cartItems = cartItems.replace(
            "]",
            connector + newItem + "]",
          )
          println( "=== " + cartItems);
          session.set("cartItems", cartItems)
        }
      )
      .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
      .exec(calculateCartRequest)
      .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
    }
    .exec(checkoutDataRequest)
    .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
    .exec(checkoutRequest)
}

class GuestNonPersistentCartFlowApiRamp extends Simulation with GuestNonPersistentCartFlowApiBase {
  override lazy val scenarioName = "API: Guest Non-persistent Cart Flow [Incremental]"

  setUp(scn.inject(
    rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration)
  )).protocols(httpProtocol)
}

class GuestNonPersistentCartFlowApiSteady extends Simulation with GuestNonPersistentCartFlowApiBase {
  override lazy val scenarioName = "API: Guest Non-persistent Cart Flow [Steady RPS]"

  setUp(scn.inject(
    constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration)
  )).protocols(httpProtocol)
}
