package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.OrderDeliverDetails;

import java.sql.SQLException;

public interface OrderDeliverDetailDAO extends CrudDAO<OrderDeliverDetails> {

    boolean addOrderDeliveryDetails(String orderId, String deliverId) throws SQLException;

}
