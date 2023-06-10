package lk.ijse.cargoproconnect.dao.daos.impl;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.dao.daos.OrderItemCategoryDAO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;
import lk.ijse.cargoproconnect.entity.OrderItemCategory;
import lk.ijse.cargoproconnect.model.CategoryModel;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItemCategoryDAOImpl implements OrderItemCategoryDAO {

    @Override
    public ArrayList<OrderItemCategory> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean add(OrderItemCategory dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public OrderItemCategory search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean update(OrderItemCategory dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean addOrderItemCategories(String orderId, ObservableList<OrderItemTM> observableList) throws SQLException {
        for (OrderItemTM orderItemTM : observableList) {
            CrudUtil.execute("INSERT INTO order_item_category(order_id, item_category_id, item_name, note , weight, qty, total_tax) VALUES (?, ?, ?, ?, ?, ?, ?)", orderId, CategoryModel.getCategoryId(orderItemTM.getCategory()), orderItemTM.getItem(), orderItemTM.getNote(), orderItemTM.getUnitWeight(), orderItemTM.getQty(), orderItemTM.getTotalTax());
        }
        return true;
    }
}
