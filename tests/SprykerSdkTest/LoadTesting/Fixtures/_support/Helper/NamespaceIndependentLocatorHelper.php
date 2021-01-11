<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Helper;

use Codeception\TestInterface;
use SprykerTest\Shared\Testify\Helper\LocatorHelper;

class NamespaceIndependentLocatorHelper extends LocatorHelper
{
    /**
     * @param array $settings
     *
     * @return void
     */
    public function _beforeSuite($settings = []): void
    {
        $this->clearLocators();
        $this->clearCaches();
        // Skipping Namespace overridding
    }

    /**
     * @param \Codeception\TestInterface $test
     *
     * @return void
     */
    public function _before(TestInterface $test): void
    {
        $this->clearLocators();
        $this->clearCaches();
        // Skipping Namespace overridding
    }
}
