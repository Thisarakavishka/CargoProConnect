package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO {

    ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException;

    boolean addCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;

    String generateNewCustomerId() throws SQLException, ClassNotFoundException;

    boolean deleteSelectedCustomers(ArrayList<String> ids) throws SQLException;

    ArrayList<String> getCustomersEmails(ArrayList<String> ids) throws SQLException;
}
