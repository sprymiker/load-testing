const scenarios = [
    // {
    //     title: 'Storefront',
    //     tests: [
    //         { id: 'Home', title: 'Home page'},
    //         { id: 'Nope', title: 'Empty index'},
    //         { id: 'CatalogSearch', title: 'Search page'},
    //         { id: 'Pdp', title: 'PDP page'},
    //         { id: 'AddToGuestCart', title: 'Add to guest cart'},
    //         { id: 'AddToCustomerCart', title: 'Add to customer cart'},
    //         { id: 'DirectPlaceOrder', title: 'Direct Place order'},
    //         { id: 'PlaceOrderCustomer', title: 'Place order customer'},
    //     ],
    // },
    {
        title: 'API',
        tests: [
            // { id: 'CartApi', title: 'API: Add to cart'},
            { id: 'GuestCartApi', title: 'API: Add to guest cart'},
            { id: 'CatalogSearchApi', title: 'API: Search'},
            { id: 'PdpApi', title: 'API: PDP'},
            // { id: 'GuestCheckoutApi', title: 'API: Guest checkout request'},
            // { id: 'CheckoutApi', title: 'API: Checkout'},
            { id: 'DirectPlaceOrderApi', title: 'API: Direct Place order'},
            { id: 'DirectPlaceOrderBaseApiWithMixedQuantity', title: 'API: Direct Place order with mixed quantity (x70)'},
            { id: 'GuestCartFlowApi', title: 'API: Guest Cart Flow'},
        ],
    },
];

module.exports = scenarios;
