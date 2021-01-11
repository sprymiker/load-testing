<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Glue\LoadTesting\Controller;

use Generated\Shared\Transfer\LoadTestingPlaceOrderRequestAttributesTransfer;
use Spryker\Glue\GlueApplication\Rest\JsonApi\RestResponseInterface;
use Spryker\Glue\GlueApplication\Rest\Request\Data\RestRequestInterface;
use Spryker\Glue\Kernel\Controller\AbstractController;

/**
 * @method \SprykerSdk\Glue\LoadTesting\LoadTestingFactory getFactory()
 */
class PlaceOrderTestResourceController extends AbstractController
{
    /**
     * @Glue({
     *     "post": {
     *          "summary": [
     *              "Place an order based on provided payload. ONLY for testing purposes."
     *          ]
     *     }
     * })
     *
     * @param \Spryker\Glue\GlueApplication\Rest\Request\Data\RestRequestInterface $restRequest
     * @param \Generated\Shared\Transfer\LoadTestingPlaceOrderRequestAttributesTransfer $loadTestingPlaceOrderAttributes
     *
     * @return \Spryker\Glue\GlueApplication\Rest\JsonApi\RestResponseInterface
     */
    public function postAction(
        RestRequestInterface $restRequest,
        LoadTestingPlaceOrderRequestAttributesTransfer $loadTestingPlaceOrderAttributes
    ): RestResponseInterface {
        return $this->getFactory()->createPlaceOrderProcessor()->placeOrder($restRequest, $loadTestingPlaceOrderAttributes);
    }
}
