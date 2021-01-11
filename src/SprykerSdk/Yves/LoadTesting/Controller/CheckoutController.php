<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Yves\LoadTesting\Controller;

use Spryker\Yves\Kernel\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;

/**
 * @method \SprykerSdk\Yves\LoadTesting\LoadTestingFactory getFactory()
 * @method \SprykerSdk\Client\LoadTesting\LoadTestingClientInterface getClient()
 */
class CheckoutController extends AbstractController
{
    /**
     * @param \Symfony\Component\HttpFoundation\Request $request
     *
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    public function placeOrderTestAction(Request $request): JsonResponse
    {
        $payload = $request->get('_payload');
        if ($payload === null) {
            return $this->jsonResponse([], 400);
        }

        $checkoutResponseTransfer = $this->getClient()->placeOrderBasedOnPayloadForTestingPurposes($payload);

        if (!$checkoutResponseTransfer->getIsSuccess()) {
            return $this->jsonResponse([], 400);
        }

        return $this->jsonResponse([], 200);
    }
}
