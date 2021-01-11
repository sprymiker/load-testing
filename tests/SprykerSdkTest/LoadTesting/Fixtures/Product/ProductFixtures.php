<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Product;

use Generated\Shared\Transfer\ProductAbstractTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Spryker\Zed\Product\Business\Exception\MissingProductException;
use Spryker\Zed\Product\Dependency\ProductEvents;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * @group SprykerSdkTest
 * @group LoadTesting
 * @group Fixtures
 * @group Product
 * @group ProductFixtures
 * Add your own group annotations below this line
 */
class ProductFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    protected const DEMO_DATA_FILE_NAME = 'product_concrete.csv';
    protected const KEY_SKU = 'sku';
    protected const KEY_PDP_URL = 'pdp_url';

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester $I
     *
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingProductTester $I): FixturesContainerInterface
    {
        $this->createProductConcrete($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester $I
     *
     * @return void
     */
    protected function createProductConcrete(LoadTestingProductTester $I): void
    {
        $demoData = $this->loadDemoData();

        $eventFacade = $I->getEventFacade();
        $productFacade = $I->getProductFacade();

        foreach ($demoData as $productConcreteData) {

            try {
                $productFacade->getProductConcrete($productConcreteData[static::KEY_SKU]);
                continue;
            } catch (MissingProductException $exception) {
                //do nothing
            }

            $productConcreteOverride = [
                ProductConcreteTransfer::SKU => $productConcreteData[static::KEY_SKU],
                ProductConcreteTransfer::IS_ACTIVE => 1,
            ];

            $productAbstractOverride = [
                ProductAbstractTransfer::SKU => 'A' . $productConcreteData[static::KEY_SKU],
            ];

            $productConcreteTransfer = $I->haveFullProductWithPrice(
                $productConcreteOverride,
                $productAbstractOverride,
                $productConcreteData[static::KEY_PDP_URL]
            );

            $eventFacade->trigger(
                ProductEvents::PRODUCT_ABSTRACT_AFTER_CREATE,
                (new ProductAbstractTransfer())
                    ->setIdProductAbstract($productConcreteTransfer->getFkProductAbstract())
            );
            $eventFacade->trigger(ProductEvents::PRODUCT_CONCRETE_AFTER_CREATE, $productConcreteTransfer);
        }
    }

    /**
     * @return string
     */
    protected function getFileName(): string
    {
        return static::DEMO_DATA_FILE_NAME;
    }

    /**
     * @return array
     */
    protected function getRequiredFields(): array
    {
        return [
            static::KEY_SKU,
            static::KEY_PDP_URL,
        ];
    }
}
