<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Client\LoadTesting;

use Generated\Shared\Transfer\CheckoutResponseTransfer;
use Spryker\Client\Kernel\AbstractClient;

/**
 * @method \SprykerSdk\Client\LoadTesting\LoadTestingFactory getFactory()
 */
class LoadTestingClient extends AbstractClient implements LoadTestingClientInterface
{
    /**
     * {@inheritDoc}
     *
     * @api
     *
     * @param string $payload
     *
     * @return \Generated\Shared\Transfer\CheckoutResponseTransfer
     */
    public function placeOrderBasedOnPayloadForTestingPurposes(string $payload): CheckoutResponseTransfer
    {
        return $this->getFactory()
            ->createPlaceOrderProcessor()
            ->placeOrder($payload);
    }
}
