package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeDAO extends CrudDAO<Employee> {

    ArrayList<Employee> getEmployees(int i) throws SQLException, ClassNotFoundException;

    boolean deleteSelectedEmployees(ArrayList<String> ids) throws SQLException;

    boolean approveEmployee(String id) throws SQLException;

    boolean updateUser(Employee employee) throws SQLException;

}
