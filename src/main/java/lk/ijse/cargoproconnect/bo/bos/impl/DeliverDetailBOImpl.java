package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.DeliverDetailBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.DeliverDetailDAO;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.entity.DeliverDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public class DeliverDetailBOImpl implements DeliverDetailBO {

    //Dependency Injection (Property Injection)
    DeliverDetailDAO deliverDetailDAO = (DeliverDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.DELIVER_DETAIL);

    @Override
    public ArrayList<DeliveryDTO> getAllDeliverDetails() throws SQLException, ClassNotFoundException {
        ArrayList<DeliveryDTO> deliveryDTOS = new ArrayList<>();
        ArrayList<DeliverDetails> details = deliverDetailDAO.getAll();
        for (DeliverDetails deliverDetails : details) {
            deliveryDTOS.add(new DeliveryDTO(deliverDetails.getId(), deliverDetails.getAddress(), deliverDetails.getContact1(), deliverDetails.getContact2(), deliverDetails.getIsDelivered(), deliverDetails.getConfirmedBy()));
        }
        return deliveryDTOS;
    }

    @Override
    public boolean addDeliverDetail(DeliveryDTO dto) throws SQLException, ClassNotFoundException {
        return deliverDetailDAO.add(new DeliverDetails(dto.getId(), dto.getAddress(), dto.getContact1(), dto.getContact2(), dto.getIsDelivered(), dto.getConfirmedBy()));
    }

    @Override
    public DeliveryDTO searchDeliverDetail(String deliverId) throws SQLException, ClassNotFoundException {
        DeliverDetails deliverDetails = deliverDetailDAO.search(deliverId);
        return new DeliveryDTO(deliverDetails.getId(), deliverDetails.getAddress(), deliverDetails.getContact1(), deliverDetails.getContact2(), deliverDetails.getIsDelivered(), deliverDetails.getConfirmedBy());
    }

    @Override
    public boolean updateDeliverDetail(DeliveryDTO dto) throws SQLException, ClassNotFoundException {
        return deliverDetailDAO.update(new DeliverDetails(dto.getId(), dto.getAddress(), dto.getContact1(), dto.getContact2(), dto.getIsDelivered(), dto.getConfirmedBy()));
    }

    @Override
    public boolean deleteDeliverDetail(String id) throws SQLException, ClassNotFoundException {
        return deliverDetailDAO.delete(id);
    }

    @Override
    public String generateNewDeliverDetailId() throws SQLException, ClassNotFoundException {
        return deliverDetailDAO.generateNewId();
    }

    @Override
    public ArrayList<DeliveryDTO> getDeliveries(int i) throws SQLException {
        ArrayList<DeliveryDTO> deliveryDTOS = new ArrayList<>();
        ArrayList<DeliverDetails> details = deliverDetailDAO.getDeliveries(i);
        for (DeliverDetails deliverDetails : details) {
            deliveryDTOS.add(new DeliveryDTO(deliverDetails.getId(), deliverDetails.getAddress(), deliverDetails.getContact1(), deliverDetails.getContact2(), deliverDetails.getIsDelivered(), deliverDetails.getConfirmedBy()));
        }
        return deliveryDTOS;
    }

    @Override
    public boolean confirmDeliver(String deliverId, String employeeId) throws SQLException {
        return deliverDetailDAO.confirmDeliver(deliverId, employeeId);
    }
}
