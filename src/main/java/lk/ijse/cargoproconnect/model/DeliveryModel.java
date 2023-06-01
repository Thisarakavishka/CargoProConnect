package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryModel {
    public static List<DeliveryDTO> getDeliveries() throws SQLException {
        String sql = "SELECT * FROM deliver_details";
        List<DeliveryDTO> deliveries = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            deliveries.add(new DeliveryDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            ));
        }
        return deliveries;
    }

    public static List<DeliveryDTO> getDeliveries(int i) throws SQLException {
        String sql = "SELECT * FROM deliver_details WHERE is_delivered = ?";
        List<DeliveryDTO> deliveries = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql, i);
        while (resultSet.next()) {
            deliveries.add(new DeliveryDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            ));
        }
        return deliveries;
    }

    public static boolean addDeliveryDetails(DeliveryDTO delivery) throws SQLException {
        String sql = "INSERT INTO deliver_details(deliver_id, address, contact_1, contact_2) VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql, delivery.getId(), delivery.getAddress(), delivery.getContact1(), delivery.getContact2());
    }

    public static boolean addOrderDeliveryDetails(String orderId, String id) throws SQLException {
        String sql = "INSERT INTO order_deliver_details(order_id, deliver_id) VALUES (?, ?)";
        return CrudUtil.execute(sql, orderId, id);
    }

    public static String getNextDeliverId() throws SQLException {
        String sql = "SELECT MAX(deliver_id) FROM deliver_details";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "D0001";
        }
        return "D" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    public static String getOrderId(String id) throws SQLException {
        String sql = "SELECT * FROM order_deliver_details WHERE deliver_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }

    public static boolean confirmDeliver(String id, String employeeId) throws SQLException {
        String sql = "UPDATE deliver_details SET is_delivered = 1 , confirmed_by = ? WHERE deliver_id = ?";
        return CrudUtil.execute(sql, employeeId, id);
    }

    public static String getDeliverId(String id) throws SQLException {
        String sql = "SELECT order_id FROM order_deliver_details WHERE deliver_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return "";
    }

    public static DeliveryDTO getDelivery(String deliverId) throws SQLException {
        String sql = "SELECT * FROM  deliver_details WHERE deliver_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, deliverId);
        if(resultSet.next()){
            return new DeliveryDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }
}
