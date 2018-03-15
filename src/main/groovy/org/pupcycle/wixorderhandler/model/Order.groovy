package org.pupcycle.wixorderhandler.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Order model.
 *
 * @author Joe Cowman
 */
@EqualsAndHashCode
@ToString
@CompileStatic
class Order {
    String orderNumber
    String orderPlaced

    String emailAddress
    List<String> billingInfo
    List<String> shippingInfo

    String subtotal
    String shipping
    String discount
    String tax
    String total

    String buyerNote

    //Todo multiple order contents
}
