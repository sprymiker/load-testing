<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Client\LoadTesting;

use Spryker\Client\Kernel\AbstractDependencyProvider;
use Spryker\Client\Kernel\Container;
use SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientBridge;
use SprykerSdk\Client\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface;
use SprykerSdk\Client\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceBridge;
use SprykerSdk\Client\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface;

class LoadTestingDependencyProvider extends AbstractDependencyProvider
{
    public const CLIENT_CHECKOUT = 'CLIENT_CHECKOUT';
    public const CLIENT_QUOTE = 'CLIENT_QUOTE';

    public const SERVICE_UTIL_ENCODING = 'SERVICE_UTIL_ENCODING';

    /**
     * @param \Spryker\Client\Kernel\Container $container
     *
     * @return \Spryker\Client\Kernel\Container
     */
    public function provideServiceLayerDependencies(Container $container): Container
    {
        $container = parent::provideServiceLayerDependencies($container);

        $container = $this->addCheckoutClient($container);
        $container = $this->addUtilEncodingService($container);

        return $container;
    }

    /**
     * @param \Spryker\Glue\Kernel\Container $container
     *
     * @return \Spryker\Glue\Kernel\Container
     */
    protected function addCheckoutClient(Container $container): Container
    {
        $container->set(static::CLIENT_CHECKOUT, function (Container $container): LoadTestingToCheckoutClientInterface {
            return new LoadTestingToCheckoutClientBridge(
                $container->getLocator()->checkout()->client()
            );
        });

        return $container;
    }

    /**
     * @param \Spryker\Glue\Kernel\Container $container
     *
     * @return \Spryker\Glue\Kernel\Container
     */
    protected function addUtilEncodingService(Container $container): Container
    {
        $container->set(static::SERVICE_UTIL_ENCODING, function (Container $container): LoadTestingToUtilEncodingServiceInterface {
            return new LoadTestingToUtilEncodingServiceBridge($container->getLocator()->utilEncoding()->service());
        });

        return $container;
    }
}
