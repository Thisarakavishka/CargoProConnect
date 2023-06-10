package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.Order;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO extends CrudDAO<Order> {

    ArrayList<Order> getOrders(int i) throws SQLException;

    boolean orderIsChecked(String orderId) throws SQLException;

    boolean updateIsDeliver(String orderId) throws SQLException;

}
