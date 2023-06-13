package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.EmployeeBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.EmployeeDAO;
import lk.ijse.cargoproconnect.dto.EmployeeDTO;
import lk.ijse.cargoproconnect.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeBOImpl implements EmployeeBO {

    //Dependency Injection (Property Injection)
    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.EMPLOYEE);

    @Override
    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException {
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        ArrayList<Employee> employees = employeeDAO.getAll();
        for (Employee employee : employees) {
            employeeDTOS.add(new EmployeeDTO(employee.getId(), employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getDocumentType(), employee.getDocumentNumber(), employee.getStatus()));
        }
        return employeeDTOS;
    }

    @Override
    public boolean addEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        return employeeDAO.add(new Employee(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getDocumentType(), dto.getDocumentNumber(), dto.getStatus()));
    }

    @Override
    public EmployeeDTO searchEmployee(String id) throws SQLException, ClassNotFoundException {
        Employee employee = employeeDAO.search(id);
        return new EmployeeDTO(employee.getId(), employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getDocumentType(), employee.getDocumentNumber(), employee.getStatus());
    }

    @Override
    public boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        return employeeDAO.update(new Employee(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getDocumentType(), dto.getDocumentNumber(), dto.getStatus()));
    }

    @Override
    public boolean deleteEmployee(String id) throws SQLException, ClassNotFoundException {
        return employeeDAO.delete(id);
    }

    @Override
    public String generateNewEmployeeId() throws SQLException, ClassNotFoundException {
        return employeeDAO.generateNewId();
    }

    @Override
    public ArrayList<EmployeeDTO> getEmployees(int i) throws SQLException, ClassNotFoundException {
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        ArrayList<Employee> employees = employeeDAO.getEmployees(i);
        for (Employee employee : employees) {
            employeeDTOS.add(new EmployeeDTO(employee.getId(), employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getDocumentType(), employee.getDocumentNumber(), employee.getStatus()));
        }
        return employeeDTOS;
    }

    @Override
    public boolean deleteSelectedEmployees(ArrayList<String> ids) throws SQLException {
        return employeeDAO.deleteSelectedEmployees(ids);
    }

    @Override
    public boolean approveEmployee(String id) throws SQLException {
        return employeeDAO.approveEmployee(id);
    }

    @Override
    public boolean updateUser(EmployeeDTO employee) throws SQLException {
        return employeeDAO.updateUser(new Employee(employee.getId(), employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getDocumentType(), employee.getDocumentNumber(), employee.getStatus()));
    }
}
