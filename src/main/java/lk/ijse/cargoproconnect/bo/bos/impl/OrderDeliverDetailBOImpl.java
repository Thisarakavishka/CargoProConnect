package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.OrderDeliverDetailBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.DeliverDetailDAO;
import lk.ijse.cargoproconnect.dao.daos.OrderDAO;
import lk.ijse.cargoproconnect.dao.daos.OrderDeliverDetailDAO;
import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.OrderDeliverDetailsDTO;
import lk.ijse.cargoproconnect.entity.DeliverDetails;
import lk.ijse.cargoproconnect.entity.OrderDeliverDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDeliverDetailBOImpl implements OrderDeliverDetailBO {

    //Dependency Injection (Property Injection)
    OrderDeliverDetailDAO deliverDetailDAO = (OrderDeliverDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DELIVER_DETAIL);
    DeliverDetailDAO detailDAO = (DeliverDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.DELIVER_DETAIL);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public boolean add(OrderDeliverDetailsDTO dto) throws SQLException, ClassNotFoundException {
        return deliverDetailDAO.add(new OrderDeliverDetails(dto.getOrderId(), dto.getDeliverId()));
    }

    @Override
    public OrderDeliverDetailsDTO search(String id) throws SQLException, ClassNotFoundException {
        OrderDeliverDetails deliverDetails = deliverDetailDAO.search(id);
        return new OrderDeliverDetailsDTO(deliverDetails.getOrderId(), deliverDetails.getDeliverId());
    }

    //Transaction
    @Override
    public ArrayList<DeliveryDTO> getDeliveries(int i) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<DeliverDetails> deliveries = detailDAO.getDeliveries(i);
            ArrayList<DeliveryDTO> checkedDeliveryList = new ArrayList<>();
            if (!deliveries.isEmpty()) {
                for (DeliverDetails delivery : deliveries) {

                    OrderDeliverDetails deliverDetails = deliverDetailDAO.search(delivery.getId());
                    String orderId = deliverDetails.getOrderId();
                    if (!orderId.isEmpty() || !orderId.isBlank()) {
                        boolean isChecked = orderDAO.orderIsChecked(orderId);
                        if (isChecked) {
                            checkedDeliveryList.add(new DeliveryDTO(delivery.getId(), delivery.getAddress(), delivery.getContact1(), delivery.getContact2(), delivery.getIsDelivered(), delivery.getConfirmedBy()));
                            connection.commit();
                        }
                    }
                }
            }
            return checkedDeliveryList;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
            return null;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //Transaction
    @Override
    public boolean confirmDeliver(String id, String employeeId) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOkay = detailDAO.confirmDeliver(id, employeeId);
            if (isOkay) {
                OrderDeliverDetails deliverDetails = deliverDetailDAO.search(id);
                String orderId = deliverDetails.getOrderId();
                if (!orderId.isEmpty()) {
                    boolean isUpdate = orderDAO.updateIsDeliver(orderId);
                    if (isUpdate) {
                        return true;
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
