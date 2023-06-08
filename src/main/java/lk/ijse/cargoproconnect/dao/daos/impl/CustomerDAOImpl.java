package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.CustomerDAO;
import lk.ijse.cargoproconnect.entity.Customer;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer> getAll() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        while (resultSet.next()) {
            customers.add(new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8)));
        }
        return customers;
    }

    @Override
    public boolean add(Customer dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO customer(customer_id, last_name, first_name, contact_number_1, contact_number_2, document_type, document_number, email)VALUES(?,?,?,?,?,?,?,?)", dto.getId(), dto.getLName(), dto.getFName(), dto.getContactN1(), dto.getContactN2(), dto.getDocumentType(), dto.getDocumentNumber(), dto.getEmail());
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE customer_id = ?", id);
        if (resultSet.next()) {
            return new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));
        }
        return null;
    }

    @Override
    public boolean update(Customer dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE customer SET first_name = ?, last_name = ?, email = ?, contact_number_1 = ?, contact_number_2 = ?, document_type = ?, document_number = ? WHERE customer_id = ?", dto.getFName(), dto.getLName(), dto.getEmail(), dto.getContactN1(), dto.getContactN2(), dto.getDocumentType(), dto.getDocumentNumber(), dto.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(customer_id) FROM customer");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "C0001";
        }
        return "C" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    @Override
    public boolean deleteSelectedCustomers(ArrayList<String> ids) throws SQLException {
        String sql = "DELETE FROM customer WHERE customer_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    @Override
    public ArrayList<String> getEmails(ArrayList<String> ids) throws SQLException {
        String sql = "SELECT email FROM customer WHERE customer_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";

        ResultSet resultSet = CrudUtil.execute(sql);
        ArrayList<String> emails = new ArrayList<>();
        while (resultSet.next()) {
            emails.add(resultSet.getString(1));
        }
        return emails;
    }

    // now you cna use search() method for get customer email before you use getEmail() method
}
