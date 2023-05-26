package lk.ijse.cargoproconnect.model;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.Delivery;
import lk.ijse.cargoproconnect.dto.Order;
import lk.ijse.cargoproconnect.dto.Payment;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderModel {

    public static Connection connection = null;

    public static boolean placeNewOrder(int totalPrice, int weight, Delivery delivery, ObservableList<OrderItemTM> observableList, Order order, double totalTax, double total, String paymentType) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String deliverDate = BatchModel.getDeliverDate(order.getBatchId());
            boolean isAddDeliver = DeliveryModel.addDeliveryDetails(delivery);
            if (isAddDeliver) {
                boolean addPaymentDetails = PaymentModel.addPaymentDetails(new Payment(order.getPaymentId(), paymentType, total, totalTax));
                if (addPaymentDetails) {
                    boolean isAdded = OrderModel.placeNewOrder(new Order(order.getId(), order.getCustomerId(), order.getPaymentId(), order.getBatchId(), order.getOrderDate(), deliverDate, weight,totalPrice));
                    if (isAdded) {
                        boolean isAddItems = OrderModel.addOrderItemCategories(order.getId(), observableList);
                        if (isAddItems) {
                            boolean isAddOrderDeliverDetails = DeliveryModel.addOrderDeliveryDetails(order.getId(), delivery.getId());
                            if (isAddOrderDeliverDetails) {
                                int currentWeight = BatchModel.getCurrentWeight(order.getBatchId());
                                boolean isAddWeight = BatchModel.updateBatchWeight(order.getBatchId(), currentWeight + weight);
                                if (isAddWeight) {
                                    int currentOrders = BatchModel.getCurrentOrders(order.getBatchId());
                                    boolean isAddOrders = BatchModel.setCurrentOrders(order.getBatchId(), currentOrders + 1);
                                    if (isAddOrders) {
                                        BatchModel.setStatus();
                                        connection.commit();
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
