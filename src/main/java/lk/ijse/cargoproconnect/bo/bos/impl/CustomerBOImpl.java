package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.CustomerBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.CustomerDAO;
import lk.ijse.cargoproconnect.dto.CustomerDTO;
import lk.ijse.cargoproconnect.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    //Dependency Injection (Property Injection)
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customers = customerDAO.getAll();
        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOS.add(new CustomerDTO(customer.getId(),customer.getFName(),customer.getLName(),customer.getContactN1(),customer.getContactN2(),customer.getDocumentType(),customer.getDocumentNumber(),customer.getEmail()));
        }
        return customerDTOS;
    }

    @Override
    public boolean addCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(dto.getId(),dto.getFName(),dto.getLName(),dto.getContactN1(),dto.getContactN2(),dto.getDocumentType(),dto.getDocumentNumber(),dto.getEmail()));
    }

    @Override
    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer customer = customerDAO.search(id);
        return new CustomerDTO(customer.getId(),customer.getFName(),customer.getLName(),customer.getContactN1(),customer.getContactN2(),customer.getDocumentType(),customer.getDocumentNumber(),customer.getEmail());
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getId(),dto.getFName(),dto.getLName(),dto.getContactN1(),dto.getContactN2(),dto.getDocumentType(),dto.getDocumentNumber(),dto.getEmail()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public String generateNewCustomerId() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewId();
    }

    @Override
    public boolean deleteSelectedCustomers(ArrayList<String> ids) throws SQLException {
        return customerDAO.deleteSelectedCustomers(ids);
    }

    @Override
    public ArrayList<String> getCustomersEmails(ArrayList<String> ids) throws SQLException {
        return customerDAO.getEmails(ids);
    }
}
