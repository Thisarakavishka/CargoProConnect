package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.OrderDAO;
import lk.ijse.cargoproconnect.entity.Order;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public ArrayList<Order> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Order> orders = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders ");
        while (resultSet.next()) {
            orders.add(new Order(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getString(10), resultSet.getInt(11), resultSet.getDouble(12)));
        }
        return orders;
    }

    @Override
    public boolean add(Order dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orders(order_id, customer_id, batch_id, order_date, deliver_date, payment_id, weight, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", dto.getId(), dto.getCustomerId(), dto.getBatchId(), dto.getOrderDate(), dto.getDeliverDate(), dto.getPaymentId(), dto.getWeight(), dto.getTotalPrice());
    }

    @Override
    public Order search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders WHERE order_id = ?", id);
        if (resultSet.next()) {
            return new Order(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getString(10), resultSet.getInt(11), resultSet.getDouble(12));
        }
        return null;
    }

    @Override
    public boolean update(Order dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE orders set is_checked = 1, check_by = ? ,check_time = ? WHERE order_id = ?";
        return CrudUtil.execute(sql, dto.getCheckBy(), dto.getCheckTime(), dto.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM orders WHERE order_id = ? ", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(order_id) FROM orders");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "O0001";
        }
        return "O" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    @Override
    public ArrayList<Order> getOrders(int i) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders WHERE is_checked = ?", i);
        while (resultSet.next()) {
            orders.add(new Order(
                    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getString(10), resultSet.getInt(11), resultSet.getDouble(12)));
        }
        return orders;
    }

    @Override
    public boolean orderIsChecked(String orderId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT is_checked FROM orders WHERE order_id = ?", orderId);
        if (resultSet.next()) {
            if (resultSet.getString(1).equalsIgnoreCase("1")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateIsDeliver(String orderId) throws SQLException {
        return CrudUtil.execute("UPDATE orders set is_deliver = 1 WHERE order_id = ?", orderId);
    }

    @Override
    public boolean checkOrder(String orderId, String employeeId, String time) throws SQLException {
        return CrudUtil.execute("UPDATE orders set is_checked = 1, check_by = ? ,check_time = ? WHERE order_id = ?", employeeId, time, orderId);
    }

}
