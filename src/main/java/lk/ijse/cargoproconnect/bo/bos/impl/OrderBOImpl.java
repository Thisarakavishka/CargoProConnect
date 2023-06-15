package lk.ijse.cargoproconnect.bo.bos.impl;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.bos.OrderBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.*;
import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.OrderDTO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;
import lk.ijse.cargoproconnect.entity.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBOImpl implements OrderBO {

    //Dependency Injection (Property Injection)
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);
    DeliverDetailDAO detailDAO = (DeliverDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.DELIVER_DETAIL);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    OrderItemCategoryDAO orderItemCategoryDAO = (OrderItemCategoryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_ITEM_CATEGORY);
    OrderDeliverDetailDAO deliverDetailDAO = (OrderDeliverDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DELIVER_DETAIL);

    @Override
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        ArrayList<Order> orders = orderDAO.getAll();
        for (Order order : orders) {
            orderDTOS.add(new OrderDTO(order.getId(), order.getCustomerId(), order.getPaymentId(), order.getBatchId(), order.getOrderDate(), order.getIsChecked(), order.getCheckBy(), order.getCheckTime(), order.getIsDeliver(), order.getDeliverDate(), order.getWeight(), order.getTotalPrice()));
        }
        return orderDTOS;
    }

    @Override
    public boolean addOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        return orderDAO.add(new Order(dto.getId(), dto.getCustomerId(), dto.getPaymentId(), dto.getBatchId(), dto.getOrderDate(), dto.getIsChecked(), dto.getCheckBy(), dto.getCheckTime(), dto.getIsDeliver(), dto.getDeliverDate(), dto.getWeight(), dto.getTotalPrice()));
    }

    @Override
    public OrderDTO searchOrder(String id) throws SQLException, ClassNotFoundException {
        Order order = orderDAO.search(id);
        return new OrderDTO(order.getId(), order.getCustomerId(), order.getPaymentId(), order.getBatchId(), order.getOrderDate(), order.getIsChecked(), order.getCheckBy(), order.getCheckTime(), order.getIsDeliver(), order.getDeliverDate(), order.getWeight(), order.getTotalPrice());
    }

    @Override
    public boolean updateOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        return orderDAO.update(new Order(dto.getId(), dto.getCustomerId(), dto.getPaymentId(), dto.getBatchId(), dto.getOrderDate(), dto.getIsChecked(), dto.getCheckBy(), dto.getCheckTime(), dto.getIsDeliver(), dto.getDeliverDate(), dto.getWeight(), dto.getTotalPrice()));
    }

    @Override
    public boolean deleteOrder(String id) throws SQLException, ClassNotFoundException {
        return orderDAO.delete(id);
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewId();
    }

    @Override
    public ArrayList<OrderDTO> getOrders(int i) throws SQLException {
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        ArrayList<Order> orders = orderDAO.getOrders(i);
        for (Order order : orders) {
            orderDTOS.add(new OrderDTO(order.getId(), order.getCustomerId(), order.getPaymentId(), order.getBatchId(), order.getOrderDate(), order.getIsChecked(), order.getCheckBy(), order.getCheckTime(), order.getIsDeliver(), order.getDeliverDate(), order.getWeight(), order.getTotalPrice()));
        }
        return orderDTOS;
    }

    @Override
    public boolean orderIsChecked(String orderId) throws SQLException {
        return orderDAO.orderIsChecked(orderId);
    }

    @Override
    public boolean updateIsDeliver(String orderId) throws SQLException {
        return orderDAO.updateIsDeliver(orderId);
    }

    //Transaction
    @Override
    public boolean placeNewOrder(int totalPrice, int weight, DeliveryDTO delivery, ObservableList<OrderItemTM> observableList, OrderDTO order, double totalTax, double total, String paymentType) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            Batch batch = batchDAO.search(order.getBatchId());
            String deliverDate = batch.getDDate();
            boolean isAddDeliver = detailDAO.add(new DeliverDetails(delivery.getId(), delivery.getAddress(), delivery.getContact1(), delivery.getContact2(), delivery.getIsDelivered(), delivery.getConfirmedBy()));
            if (isAddDeliver) {
                boolean addPaymentDetails = paymentDAO.add(new Payment(order.getPaymentId(), paymentType, total, totalTax));
                if (addPaymentDetails) {
                    boolean isAdded = orderDAO.add(new Order(order.getId(), order.getCustomerId(), order.getPaymentId(), order.getBatchId(), order.getOrderDate(), deliverDate, weight, totalPrice));
                    if (isAdded) {
                        boolean isAddItems = orderItemCategoryDAO.addOrderItemCategories(order.getId(), observableList);
                        if (isAddItems) {
                            boolean isAddOrderDeliverDetails = deliverDetailDAO.add(new OrderDeliverDetails(order.getId(), delivery.getId()));
                            if (isAddOrderDeliverDetails) {
                                Batch searchBatch = batchDAO.search(order.getBatchId());
                                int currentWeight = searchBatch.getCurrentWeight();
                                boolean isAddWeight = batchDAO.updateBatchWeight(order.getBatchId(), currentWeight + weight);
                                if (isAddWeight) {
                                    Batch searchBatch2 = batchDAO.search(order.getBatchId());
                                    int currentOrders = searchBatch2.getNoOfOrders();
                                    boolean isAddOrders = batchDAO.setCurrentOrders(order.getBatchId(), currentOrders + 1);
                                    if (isAddOrders) {
                                        batchDAO.setStatus();
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
