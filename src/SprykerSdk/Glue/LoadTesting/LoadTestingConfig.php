<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Glue\LoadTesting;

use Spryker\Glue\Kernel\AbstractBundleConfig;

class LoadTestingConfig extends AbstractBundleConfig
{
    public const RESOURCE_PLACE_ORDER_TEST = 'place-order-test';

    public const CONTROLLER_PLACE_ORDER_TEST = 'place-order-test-resource';

    public const ACTION_PLACE_ORDER_TEST_POST = 'post';
}
