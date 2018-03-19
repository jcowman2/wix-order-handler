package org.pupcycle.wixorderhandler.util

import org.pupcycle.wixorderhandler.exception.NoMatchFoundException
import org.pupcycle.wixorderhandler.model.Email
import org.pupcycle.wixorderhandler.model.Order
import org.pupcycle.wixorderhandler.model.OrderItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.pupcycle.wixorderhandler.util.ParseUtil.extractMultilineValue
import static org.pupcycle.wixorderhandler.util.ParseUtil.extractValue

/**
 * Creates orders given a Wix order email.
 *
 * @author Joe Cowman
 */
class OrderBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBuilder.class)

    /**
     * Create an order given a Wix order email
     * @param email     the Wix email
     * @return an order with parsed info
     */
    static Order createOrder(Email email) {
        String b = email.getBody()

        List<String> billInfo = extractMultilineValue(b, 'Billing Information', 'Shipping Information')
        List<String> shipInfo = extractMultilineValue(b, 'Shipping Information', 'Delivery Method')

        Order order = new Order(
                orderNumber: extractValue(b, 'Order #', true),
                orderPlaced: extractValue(b, 'Order placed:'),
                emailAddress: billInfo.last(),
                billingInfo: billInfo.subList(0, billInfo.size() - 1),
                shippingInfo: shipInfo,
                subtotal: extractValue(b, 'Subtotal'),
                shipping: extractValue(b, 'Shipping'),
                discount: extractValue(b, 'Discount', false, false),
                tax: extractValue(b, 'Tax'),
                total: extractValue(b, 'Total cost:'),
                buyerNote: extractMultilineValue(b, '\\*Note from buyer:\\*', 'Subtotal', false).join(' '),
                orderItems: createOrderItems(b)
        )

        LOG.info("Generated order {number: ${order.orderNumber}}.")
        return order
    }

    /**
     * Creates a list of Wix order items from the Email message body
     * @param messageBody   the message body
     * @return  a list of order items
     */
    static List<OrderItem> createOrderItems(String messageBody) {
        List<String> orderDetails
        try {
            orderDetails = extractMultilineValue(messageBody, 'Item Qty Total', '\\*Note from buyer:\\*')
        } catch (NoMatchFoundException e) {
            LOG.debug('No note from buyer while generating order items. Expanding search to Subtotal.', e)
            orderDetails = extractMultilineValue(messageBody, 'Item Qty Total', 'Subtotal')
        }

        List<OrderItem> orderItems = []
        int lastStartIndex = 0
        for (int i = 0; i < orderDetails.size(); i++) {
            if (orderDetails[i].startsWith('Price')) {
                List<String> priceParts = extractValue(orderDetails[i], 'Price:').split(' ')
                orderItems << new OrderItem(
                        name: orderDetails[lastStartIndex],
                        sku: extractValue(orderDetails[lastStartIndex + 1], 'SKU:'),
                        colorPreference: extractValue(orderDetails.subList(lastStartIndex + 2, i).join(' '), '.*color preference\\(s\\):', false, false),
                        unitPrice: priceParts[0],
                        quantity: priceParts[1],
                        totalPrice: priceParts[2]
                )
                lastStartIndex = i + 1
            }
        }
        return orderItems
    }

}
