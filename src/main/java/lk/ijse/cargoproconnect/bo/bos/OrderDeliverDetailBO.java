package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.OrderDeliverDetailsDTO;

import java.sql.SQLException;

public interface OrderDeliverDetailBO {

    boolean add(OrderDeliverDetailsDTO dto) throws SQLException, ClassNotFoundException;

    OrderDeliverDetailsDTO search(String id) throws SQLException, ClassNotFoundException;
}
