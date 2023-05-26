package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.dto.Payment;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentModel {
    public static String getNextPaymentId() throws SQLException {
            String sql = "SELECT MAX(payment_id) FROM payment";
            ResultSet resultSet = CrudUtil.execute(sql);

            String currentMaxId = null;
            if (resultSet.next()) {
                currentMaxId = resultSet.getString(1);
            }
            if (currentMaxId == null) {
                return "P0001";
            }
            return "P" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
        }

    public static boolean addPaymentDetails(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment(payment_id, payment_type, total, total_tax) VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql, payment.getId(), payment.getPaymentType(), payment.getTotal(),payment.getTotalTax());
    }
}
