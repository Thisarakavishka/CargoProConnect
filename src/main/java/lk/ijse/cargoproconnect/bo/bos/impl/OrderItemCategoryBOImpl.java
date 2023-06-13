package lk.ijse.cargoproconnect.bo.bos.impl;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.bos.OrderItemCategoryBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.OrderItemCategoryDAO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;

import java.sql.SQLException;

public class OrderItemCategoryBOImpl implements OrderItemCategoryBO {

    //Dependency Injection (Property Injection)
    OrderItemCategoryDAO itemCategoryDAO = (OrderItemCategoryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_ITEM_CATEGORY);

    @Override
    public boolean addOrderItemCategories(String orderId, ObservableList<OrderItemTM> observableList) throws SQLException {
        return itemCategoryDAO.addOrderItemCategories(orderId, observableList);
    }
}
