package lk.ijse.cargoproconnect.bo.bos;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;

import java.sql.SQLException;

public interface OrderItemCategoryBO extends SuperBO {

    boolean addOrderItemCategories(String orderId, ObservableList<OrderItemTM> observableList) throws SQLException;
}
