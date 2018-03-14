package org.pupcycle.wixorderhandler.engine

import org.pupcycle.wixorderhandler.model.Email
import org.pupcycle.wixorderhandler.model.Order
import org.springframework.stereotype.Component

import static org.pupcycle.wixorderhandler.util.ParseUtil.extractMultilineValue
import static org.pupcycle.wixorderhandler.util.ParseUtil.extractValue

/**
 * Creates orders given a Wix order email.
 *
 * @author Joe Cowman
 */
@Component
class OrderEngine {

    /**
     * Create an order given a Wix order email
     * @param email     the Wix email
     * @return an order with parsed info
     */
    Order createOrder(Email email) {
        String b = email.getBody()

        List<String> billInfo = extractMultilineValue(b, 'Billing Information', 'Shipping Information')
        List<String> shipInfo = extractMultilineValue(b, 'Shipping Information', 'Delivery Method')

        billInfo.removeIf({s -> s.startsWith('<')})
        shipInfo.removeIf({s -> s.startsWith('<')})

        return new Order(
                orderNumber: extractValue(b, 'Order #', true),
                orderPlaced: extractValue(b, 'Order placed:'),
                emailAddress: billInfo.last(),
                billingInfo: billInfo.subList(0, billInfo.size() - 1),
                shippingInfo: shipInfo,
                subtotal: extractValue(b, 'Subtotal'),
                shipping: extractValue(b, 'Shipping'),
                tax: extractValue(b, 'Tax'),
                total: extractValue(b, 'Total cost:'),
                buyerNote: extractMultilineValue(b, '\\*Note from buyer:\\*', 'Subtotal', false).join(' ')
        )
    }

}
