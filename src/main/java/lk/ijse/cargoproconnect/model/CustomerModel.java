package lk.ijse.cargoproconnect.model;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.dto.Customer;
import lk.ijse.cargoproconnect.dto.tm.OrderTM;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public static List<Customer> getCustomers() throws SQLException {
        String sql = "SELECT * FROM customer";
        List<Customer> customers = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            customers.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return customers;
    }

    public static String getNextTaxId() throws SQLException {
        String sql = "SELECT MAX(customer_id) FROM customer";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "C0001";
        }
        return "C" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    public static List<String> getTypes() throws SQLException {
        String sql = "SELECT DISTINCT document_type FROM customer";
        List<String> types = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            types.add(resultSet.getString(1));
        }
        return types;
    }

    public static boolean addNewCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer(customer_id, last_name, first_name, contact_number_1, contact_number_2, document_type, document_number, email)VALUES(?,?,?,?,?,?,?,?)";
        return CrudUtil.execute(sql, customer.getId(), customer.getLName(), customer.getFName(), customer.getContactN1(), customer.getContactN2(), customer.getDocumentType(), customer.getDocumentNumber(), customer.getEmail());
    }

    public static boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET first_name = ?, last_name = ?, email = ?, contact_number_1 = ?, contact_number_2 = ?, document_type = ?, document_number = ? WHERE customer_id = ?";
        return CrudUtil.execute(sql, customer.getFName(), customer.getLName(), customer.getEmail(), customer.getContactN1(), customer.getContactN2(), customer.getDocumentType(), customer.getDocumentNumber(), customer.getId());
    }

    public static boolean deleteCustomer(String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static boolean deleteSelectedCustomers(List<String> ids) throws SQLException {
        String sql = "DELETE FROM customer WHERE customer_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    public static List<String> getEmails(List<String> ids) throws SQLException {
        String sql = "SELECT email FROM customer WHERE customer_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";

        ResultSet resultSet = CrudUtil.execute(sql);
        List<String> emails = new ArrayList<>();
        while (resultSet.next()) {
            emails.add(resultSet.getString(1));
        }
        return emails;
    }

    public static String getEmail(String customerId) throws SQLException {
        String sql = "SELECT email FROM customer WHERE customer_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, customerId);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return resultSet.getString(1);
    }

    public static Customer getCustomer(String customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, customerId);
        if(resultSet.next()){
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            );
        }
        return null;
    }
}
