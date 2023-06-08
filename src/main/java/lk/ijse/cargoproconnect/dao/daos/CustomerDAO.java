package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer> {

    boolean deleteSelectedCustomers(ArrayList<String> ids) throws SQLException;

    ArrayList<String> getEmails(ArrayList<String> ids) throws SQLException;

}
