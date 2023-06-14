package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.OrderDeliverDetailBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.OrderDeliverDetailDAO;
import lk.ijse.cargoproconnect.dto.OrderDeliverDetailsDTO;
import lk.ijse.cargoproconnect.entity.OrderDeliverDetails;

import java.sql.SQLException;

public class OrderDeliverDetailBOImpl implements OrderDeliverDetailBO {

    //Dependency Injection (Property Injection)
    OrderDeliverDetailDAO deliverDetailDAO = (OrderDeliverDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DELIVER_DETAIL);

    @Override
    public boolean add(OrderDeliverDetailsDTO dto) throws SQLException, ClassNotFoundException {
        return deliverDetailDAO.add(new OrderDeliverDetails(dto.getOrderId(), dto.getDeliverId()));
    }

    @Override
    public OrderDeliverDetailsDTO search(String id) throws SQLException, ClassNotFoundException {
        OrderDeliverDetails deliverDetails = deliverDetailDAO.search(id);
        return new OrderDeliverDetailsDTO(deliverDetails.getOrderId(), deliverDetails.getDeliverId());
    }
}
