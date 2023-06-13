package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeBO {

    ArrayList<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException;

    boolean addEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException;

    EmployeeDTO searchEmployee(String id) throws SQLException, ClassNotFoundException;

    boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteEmployee(String id) throws SQLException, ClassNotFoundException;

    String generateNewEmployeeId() throws SQLException, ClassNotFoundException;

    ArrayList<EmployeeDTO> getEmployees(int i) throws SQLException, ClassNotFoundException;

    boolean deleteSelectedEmployees(ArrayList<String> ids) throws SQLException;

    boolean approveEmployee(String id) throws SQLException;

    boolean updateUser(EmployeeDTO employee) throws SQLException;
}
