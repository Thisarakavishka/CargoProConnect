package lk.ijse.cargoproconnect.dao.daos;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;
import lk.ijse.cargoproconnect.entity.OrderItemCategory;

import java.sql.SQLException;

public interface OrderItemCategoryDAO extends CrudDAO<OrderItemCategory> {

    boolean addOrderItemCategories(String orderId, ObservableList<OrderItemTM> observableList) throws SQLException;

}
