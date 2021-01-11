<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Client\LoadTesting\Processor\Checkout;

use Generated\Shared\Transfer\CheckoutResponseTransfer;
use SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToQuoteClientInterface;

interface PlaceOrderProcessorInterface
{
    /**
     * @param string $payload
     *
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    public function placeOrder(string $payload): CheckoutResponseTransfer;
}
