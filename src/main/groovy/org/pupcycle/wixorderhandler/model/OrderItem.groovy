package org.pupcycle.wixorderhandler.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Model for an ordered item from a Wix store.
 *
 * @author Joe Cowman
 */
@ToString
@EqualsAndHashCode
@CompileStatic
class OrderItem {
    String name
    String sku
    String colorPreference
    String unitPrice
    String quantity
    String totalPrice
}
