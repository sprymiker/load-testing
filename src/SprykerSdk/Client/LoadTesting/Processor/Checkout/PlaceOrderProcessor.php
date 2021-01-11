<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Client\LoadTesting\Processor\Checkout;

use Generated\Shared\Transfer\CheckoutResponseTransfer;
use Generated\Shared\Transfer\QuoteTransfer;
use SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface;
use SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToQuoteClientInterface;
use SprykerSdk\Client\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface;

class PlaceOrderProcessor implements PlaceOrderProcessorInterface
{
    /**
     * @var \SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface
     */
    protected $checkoutClient;

    /**
     * @var \SprykerSdk\Client\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface
     */
    protected $utilEncodingService;

    /**
     * @param \SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface $checkoutClient
     * @param \SprykerSdk\Client\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface $utilEncodingService
     */
    public function __construct(
        LoadTestingToCheckoutClientInterface $checkoutClient,
        LoadTestingToUtilEncodingServiceInterface $utilEncodingService
    ) {
        $this->checkoutClient = $checkoutClient;
        $this->utilEncodingService = $utilEncodingService;
    }

    /**
     * @param string $payload
     *
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    public function placeOrder(string $payload): CheckoutResponseTransfer
    {
        $quoteArray = $this->utilEncodingService->decodeJson($payload, true);
        $quoteTransfer = (new QuoteTransfer())->fromArray($quoteArray, true);

        if (!$this->checkoutClient->isQuoteApplicableForCheckout($quoteTransfer)) {
            return (new CheckoutResponseTransfer())->setIsSuccess(false);
        }

        return $this->checkoutClient->placeOrder($quoteTransfer);
    }
}
