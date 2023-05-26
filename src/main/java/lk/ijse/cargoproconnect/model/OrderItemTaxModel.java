package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemTaxModel {

    public static Connection connection = null;

    public static double getTotalTax(String categoryId, String price, String qty) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            List<Double> percentage = new ArrayList<>();
            List<String> taxIds = TaxModel.getTaxIds(categoryId);

            if (!taxIds.isEmpty()) {
                for (String id : taxIds) {
                    percentage.add(TaxModel.getTaxPercentage(id));
                }
            }
            Double totalTax = Double.valueOf(0);
            for (Double value : percentage) {
                totalTax += Double.parseDouble(price) * (value / 100.0);
            }
            return totalTax * Integer.parseInt(qty);
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return 0;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
