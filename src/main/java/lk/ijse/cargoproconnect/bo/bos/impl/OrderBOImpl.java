package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.OrderBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.OrderDAO;
import lk.ijse.cargoproconnect.dto.OrderDTO;
import lk.ijse.cargoproconnect.entity.Order;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBOImpl implements OrderBO {

    //Dependency Injection (Property Injection)
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

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
}
