<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Client\LoadTesting;

use Generated\Shared\Transfer\CheckoutResponseTransfer;

interface LoadTestingClientInterface
{
    /**
     * Specification:
     * - Places order based on JSON encoded payload.
     * - ONLY for testing purposes.
     *
     * @api
     *
     * @param string $payload
     *
     * @return \Generated\Shared\Transfer\CheckoutResponseTransfer
     */
    public function placeOrderBasedOnPayloadForTestingPurposes(string $payload): CheckoutResponseTransfer;
}
