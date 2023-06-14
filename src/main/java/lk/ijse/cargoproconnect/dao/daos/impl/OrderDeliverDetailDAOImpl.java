package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.OrderDeliverDetailDAO;
import lk.ijse.cargoproconnect.entity.OrderDeliverDetails;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDeliverDetailDAOImpl implements OrderDeliverDetailDAO {

    @Override
    public ArrayList<OrderDeliverDetails> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean add(OrderDeliverDetails dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO order_deliver_details(order_id, deliver_id) VALUES (?, ?)", dto.getOrderId(), dto.getDeliverId());
    }

    @Override
    public OrderDeliverDetails search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM order_deliver_details WHERE deliver_id = ?", id);
        if (resultSet.next()) {
            return new OrderDeliverDetails(resultSet.getString(1),resultSet.getString(2));
        }
        return null;
    }

    @Override
    public boolean update(OrderDeliverDetails dto) throws SQLException, ClassNotFoundException {
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

    //use search() method for getOrderId(id) and getDeliverId(id)
}
