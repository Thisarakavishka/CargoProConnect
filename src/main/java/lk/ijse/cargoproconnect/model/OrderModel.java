package lk.ijse.cargoproconnect.model;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.dto.Order;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderModel {

    public static List<Order> getOrders() throws SQLException {
        String sql = "SELECT * FROM orders ";
        List<Order> orders = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            orders.add(new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getInt(9),
                    resultSet.getString(10),
                    resultSet.getInt(11),
                    resultSet.getDouble(12)
            ));
        }
        return orders;
    }

    public static List<Order> getOrders(int i) throws SQLException {
        String sql = "SELECT * FROM orders WHERE is_checked = ?";
        List<Order> orders = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql, i);
        while (resultSet.next()) {
            orders.add(new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getInt(9),
                    resultSet.getString(10),
                    resultSet.getInt(11),
                    resultSet.getDouble(12)
            ));
        }
        return orders;
    }

    public static String getNextOrderId() throws SQLException {
        String sql = "SELECT MAX(order_id) FROM orders";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "O0001";
        }
        return "O" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    public static boolean placeNewOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders(order_id, customer_id, batch_id, order_date, deliver_date, payment_id, weight, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, order.getId(), order.getCustomerId(), order.getBatchId(), order.getOrderDate(), order.getDeliverDate(), order.getPaymentId(), order.getWeight(), order.getTotalPrice());
    }

    public static boolean addOrderItemCategories(String orderId, ObservableList<OrderItemTM> observableList) throws SQLException {
        String sql = "INSERT INTO order_item_category(order_id, item_category_id, item_name, note , weight, qty, total_tax) VALUES (?, ?, ?, ?, ?, ?, ?)";
        for (OrderItemTM orderItemTM : observableList) {
            CrudUtil.execute(sql, orderId, CategoryModel.getCategoryId(orderItemTM.getCategory()), orderItemTM.getItem(), orderItemTM.getNote(),orderItemTM.getUnitWeight(), orderItemTM.getQty(), orderItemTM.getTotalTax());
        }
        return true;
    }

    public static boolean orderIsChecked(String orderId) throws SQLException {
        String sql = "SELECT is_checked FROM orders WHERE order_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, orderId);
        if (resultSet.next()) {
            if (resultSet.getString(1).equalsIgnoreCase("1")) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkOrder(String orderId, String employeeId, String time) throws SQLException {
        String sql = "UPDATE orders set is_checked = 1, check_by = ? ,check_time = ? WHERE order_id = ?";
        return CrudUtil.execute(sql, employeeId, time, orderId);
    }

    public static boolean updateIsDeliver(String orderId) throws SQLException {
        String sql = "UPDATE orders set is_deliver = 1 WHERE order_id = ?";
        return CrudUtil.execute(sql, orderId);
    }

    public static boolean deleteOrder(String id) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ? ";
        return CrudUtil.execute(sql, id);
    }

    public static Order getOrder(String id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        if(resultSet.next()){
            return new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getInt(9),
                    resultSet.getString(10),
                    resultSet.getInt(11),
                    resultSet.getDouble(12)
            );
        }
        return null;
    }
}
