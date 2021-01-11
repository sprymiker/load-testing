<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Glue\LoadTesting;

use Spryker\Glue\Kernel\AbstractFactory;
use SprykerSdk\Glue\LoadTesting\Dependency\Client\LoadTestingToPersistentCartClientInterface;
use SprykerSdk\Glue\LoadTesting\Processor\Checkout\PlaceOrderProcessor;
use SprykerSdk\Glue\LoadTesting\Processor\Checkout\PlaceOrderProcessorInterface;

/**
 * @SuppressWarnings(PHPMD.CouplingBetweenObjects)
 *
 * @method \SprykerSdk\Client\LoadTesting\LoadTestingClientInterface getClient()
 * @method \SprykerSdk\Glue\LoadTesting\LoadTestingConfig getConfig()
 */
class LoadTestingFactory extends AbstractFactory
{
    /**
     * @return \SprykerSdk\Glue\LoadTesting\Processor\Checkout\PlaceOrderProcessorInterface
     */
    public function createPlaceOrderProcessor(): PlaceOrderProcessorInterface
    {
        return new PlaceOrderProcessor(
            $this->getClient(),
            $this->getResourceBuilder()
        );
    }
}
