package org.pupcycle.wixorderhandler.config

import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.model.Order
import org.pupcycle.wixorderhandler.model.OrderItem
import org.pupcycle.wixorderhandler.model.OrderSpreadsheetEntryFactory
import org.springframework.stereotype.Component

/**
 * Default structure of an order entry in the spreadsheet.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class DefaultOrderSpreadsheetEntryFactory implements OrderSpreadsheetEntryFactory {

    @Override
    List columnNames() { [
                'Order Number',
                'Date Ordered',
                'Order Status',
                'Date Completed',
                'Quantity (S)',
                'Color Preference (S)',
                'Quantity (L)',
                'Color Preference (L)',
                'Note from Buyer',
                'Order Subtotal',
                'Paid Shipping',
                'Paid Discount',
                'Paid Tax',
                'Order Total',
                'Shipping Cost',
                'Order Fees',
                'Order Additional',
                'Net Profit',
                'Buyer Email Address',
                'Billing Information',
                'Shipping Information'
    ] }

    @Override
    List toEntry(Order order) {
        OrderItem smallToy = order.orderItems.find { it.name == 'Small Rope Dog Toy' }
        OrderItem largeToy = order.orderItems.find { it.name == 'Large Rope Dog Toy' }
        return [
                order.orderNumber,
                order.orderPlaced,
                'TODO',
                '',
                smallToy?.quantity ?: '0',
                smallToy?.colorPreference ?: 'None',
                largeToy?.quantity ?: '0',
                largeToy?.colorPreference ?: 'None',
                order.buyerNote,
                order.subtotal,
                order.shipping,
                order.discount ?: '0',
                order.tax,
                order.total,
                '',
                '',
                '',
                '', //todo calculate net profit
                order.emailAddress,
                order.billingInfo.join('  '),
                order.shippingInfo.join('  ')
        ]
    }

}
