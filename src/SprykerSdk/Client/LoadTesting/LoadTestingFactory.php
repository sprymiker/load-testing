<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Client\LoadTesting;

use Spryker\Client\Kernel\AbstractFactory;
use SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface;
use SprykerSdk\Client\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface;
use SprykerSdk\Client\LoadTesting\Processor\Checkout\PlaceOrderProcessor;
use SprykerSdk\Client\LoadTesting\Processor\Checkout\PlaceOrderProcessorInterface;

class LoadTestingFactory extends AbstractFactory
{
    public function createPlaceOrderProcessor(): PlaceOrderProcessorInterface
    {
        return new PlaceOrderProcessor(
            $this->getCheckoutClient(),
            $this->getUtilEncodingService()
        );
    }

    /**
     * @return \SprykerSdk\Glue\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface
     */
    public function getCheckoutClient(): LoadTestingToCheckoutClientInterface
    {
        return $this->getProvidedDependency(LoadTestingDependencyProvider::CLIENT_CHECKOUT);
    }

    /**
     * @return \SprykerSdk\Glue\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface
     */
    public function getUtilEncodingService(): LoadTestingToUtilEncodingServiceInterface
    {
        return $this->getProvidedDependency(LoadTestingDependencyProvider::SERVICE_UTIL_ENCODING);
    }
}
