package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.dto.Tax;
import lk.ijse.cargoproconnect.util.CrudUtil;
import org.apache.poi.ss.formula.functions.T;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaxModel {

    public static String getNextTaxId() throws SQLException {
        String sql = "SELECT MAX(tax_id) FROM tax";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "T0001";
        }
        return "T" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    public static boolean addNewTax(Tax tax) throws SQLException {
        String sql = "INSERT INTO tax(tax_id, tax_name, tax_percentage, tax_description) VALUES(?,?,?,?)";
        return CrudUtil.execute(sql, tax.getId(), tax.getName(), tax.getPercentage(), tax.getDescription());
    }

    public static List<Tax> getTaxes() throws SQLException {
        String sql = "SELECT * FROM tax";
        List<Tax> taxes = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            taxes.add(new Tax(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4)
            ));
        }
        return taxes;
    }

    public static boolean updateTax(Tax tax) throws SQLException {
        String sql = "UPDATE tax SET tax_name = ?, tax_percentage = ?, tax_description = ? WHERE tax_id = ?";
        return CrudUtil.execute(sql, tax.getName(), tax.getPercentage(), tax.getDescription(), tax.getId());
    }

    public static boolean deleteTax(String id) throws SQLException {
        String sql = "DELETE FROM tax WHERE tax_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static boolean deleteSelectedTaxes(List<String> ids) throws SQLException {
        String sql = "DELETE FROM tax WHERE tax_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    public static int getTaxCount(String categoryId) throws SQLException {
        String sql = "SELECT * FROM item_category_tax_details WHERE item_category_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, categoryId);
        int count = 0;
        while (resultSet.next()) {
            count += 1;
        }
        return count;
    }

    public static List<String> getTaxIds(String categoryId) throws SQLException {
        String sql = "SELECT tax_id FROM item_category_tax_details WHERE item_category_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, categoryId);
        List<String> taxIds = new ArrayList<>();
        while (resultSet.next()) {
            taxIds.add(resultSet.getString(1));
        }
        return taxIds;
    }

    public static Double getTaxPercentage(String id) throws SQLException {
        String sql = "SELECT tax_percentage FROM tax WHERE tax_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        Double percentage = Double.valueOf(0);
        if (resultSet.next()) {
            percentage = resultSet.getDouble(1);
        }
        return percentage;
    }

}