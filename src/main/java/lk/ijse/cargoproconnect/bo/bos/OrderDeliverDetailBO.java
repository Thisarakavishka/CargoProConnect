package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.OrderDeliverDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDeliverDetailBO extends SuperBO {

    boolean add(OrderDeliverDetailsDTO dto) throws SQLException, ClassNotFoundException;

    OrderDeliverDetailsDTO search(String id) throws SQLException, ClassNotFoundException;

    ArrayList<DeliveryDTO> getDeliveries(int i) throws SQLException;

    boolean confirmDeliver(String id, String employeeId) throws SQLException;
}
