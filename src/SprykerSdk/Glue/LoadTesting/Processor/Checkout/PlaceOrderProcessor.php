<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Glue\LoadTesting\Processor\Checkout;

use Generated\Shared\Transfer\LoadTestingPlaceOrderRequestAttributesTransfer;
use Spryker\Glue\GlueApplication\Rest\JsonApi\RestResourceBuilderInterface;
use Spryker\Glue\GlueApplication\Rest\JsonApi\RestResponseInterface;
use Spryker\Glue\GlueApplication\Rest\Request\Data\RestRequestInterface;
use SprykerSdk\Client\LoadTesting\LoadTestingClientInterface;
use SprykerSdk\Glue\LoadTesting\Processor\Mapper\CartMapperInterface;
use SprykerSdk\Glue\LoadTesting\Processor\RestResponseBuilder\GuestCartRestResponseBuilderInterface;
use Symfony\Component\HttpFoundation\Response;

class PlaceOrderProcessor implements PlaceOrderProcessorInterface
{
    /**
     * @var \SprykerSdk\Client\LoadTesting\LoadTestingClientInterface
     */
    protected $loadTestingClient;

    /**
     * @var \Spryker\Glue\GlueApplication\Rest\JsonApi\RestResourceBuilderInterface
     */
    protected $restResourceBuilder;

    /**
     * @param \SprykerSdk\Client\LoadTesting\LoadTestingClientInterface $loadTestingClient
     * @param \Spryker\Glue\GlueApplication\Rest\JsonApi\RestResourceBuilderInterface $restResourceBuilder
     */
    public function __construct(
        LoadTestingClientInterface $loadTestingClient,
        RestResourceBuilderInterface $restResourceBuilder
    ) {
        $this->loadTestingClient = $loadTestingClient;
        $this->restResourceBuilder = $restResourceBuilder;
    }

    /**
     * @param \Spryker\Glue\GlueApplication\Rest\Request\Data\RestRequestInterface $restRequest
     * @param \Generated\Shared\Transfer\LoadTestingPlaceOrderRequestAttributesTransfer $loadTestingPlaceOrderAttributesTransfer
     *
     * @return \Spryker\Glue\GlueApplication\Rest\JsonApi\RestResponseInterface
     */
    public function placeOrder(
        RestRequestInterface $restRequest,
        LoadTestingPlaceOrderRequestAttributesTransfer $loadTestingPlaceOrderAttributesTransfer
    ): RestResponseInterface {

        $payload = json_encode($loadTestingPlaceOrderAttributesTransfer->getPayload());

        $checkoutResponseTransfer = $this->loadTestingClient->placeOrderBasedOnPayloadForTestingPurposes($payload);

        if (!$checkoutResponseTransfer->getIsSuccess()) {

            return $this->restResourceBuilder->createRestResponse()
                ->setStatus(Response::HTTP_UNPROCESSABLE_ENTITY);
        }

        return $this->restResourceBuilder->createRestResponse()->setStatus(Response::HTTP_CREATED);
    }
}
