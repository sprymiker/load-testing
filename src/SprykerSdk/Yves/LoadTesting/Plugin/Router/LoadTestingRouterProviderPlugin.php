<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Yves\LoadTesting\Plugin\Router;

use Spryker\Yves\Router\Plugin\RouteProvider\AbstractRouteProviderPlugin;
use Spryker\Yves\Router\Route\RouteCollection;

class LoadTestingRouterProviderPlugin extends AbstractRouteProviderPlugin
{
    public const ROUTE_NAME_PLACE_ORDER_DEBUG = 'place-order-test';

    /**
     * @param \Spryker\Yves\Router\Route\RouteCollection $routeCollection
     *
     * @return \Spryker\Yves\Router\Route\RouteCollection
     */
    public function addRoutes(RouteCollection $routeCollection): RouteCollection
    {
        $routeCollection = $this->addPlaceOrderTestRoute($routeCollection);

        return $routeCollection;
    }

    /**
     * @param \Spryker\Yves\Router\Route\RouteCollection $routeCollection
     *
     * @return \Spryker\Yves\Router\Route\RouteCollection
     */
    protected function addPlaceOrderTestRoute(RouteCollection $routeCollection): RouteCollection
    {
        $route = $this->buildRoute('/place-order-test', 'LoadTesting', 'Checkout', 'placeOrderTestAction');
        $route = $route->setMethods(['POST']);
        $routeCollection->add(static::ROUTE_NAME_PLACE_ORDER_DEBUG, $route);

        return $routeCollection;
    }
}
