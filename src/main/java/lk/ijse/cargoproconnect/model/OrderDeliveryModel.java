package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDeliveryModel {

    public static Connection connection = null;

    public static List<DeliveryDTO> getDeliveries(int i) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            List<DeliveryDTO> deliveries = DeliveryModel.getDeliveries(i);
            List<DeliveryDTO> checkedDeliveryList = new ArrayList<>();
            if (!deliveries.isEmpty()) {
                for (DeliveryDTO delivery : deliveries) {
                    String orderId = DeliveryModel.getOrderId(delivery.getId());
                    if (!orderId.isEmpty() || !orderId.isBlank()) {
                        boolean isChecked = OrderModel.orderIsChecked(orderId);
                        if (isChecked) {
                            checkedDeliveryList.add(new DeliveryDTO(
                                    delivery.getId(),
                                    delivery.getAddress(),
                                    delivery.getContact1(),
                                    delivery.getContact2(),
                                    delivery.getIsDelivered(),
                                    delivery.getConfirmedBy()
                            ));
                            connection.commit();
                        }
                    }
                }
            }
            return checkedDeliveryList;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return null;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static boolean confirmDeliver(String id, String employeeId) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOkay = DeliveryModel.confirmDeliver(id, employeeId);
            if (isOkay) {
                String orderId = DeliveryModel.getOrderId(id);
                if (!orderId.isEmpty()) {
                    boolean isUpdate = OrderModel.updateIsDeliver(orderId);
                    if (isUpdate) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
