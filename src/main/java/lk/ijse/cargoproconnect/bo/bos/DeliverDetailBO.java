package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.DeliveryDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DeliverDetailBO {

    ArrayList<DeliveryDTO> getAllDeliverDetails() throws SQLException, ClassNotFoundException;

    boolean addDeliverDetail(DeliveryDTO dto) throws SQLException, ClassNotFoundException;

    DeliveryDTO searchDeliverDetail(String deliverId) throws SQLException, ClassNotFoundException;

    boolean updateDeliverDetail(DeliveryDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteDeliverDetail(String id) throws SQLException, ClassNotFoundException;

    String generateNewDeliverDetailId() throws SQLException, ClassNotFoundException;

    ArrayList<DeliveryDTO> getDeliveries(int i) throws SQLException;

    boolean confirmDeliver(String deliverId, String employeeId) throws SQLException;
}
