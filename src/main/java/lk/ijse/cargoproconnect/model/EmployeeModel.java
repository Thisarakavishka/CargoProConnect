package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.dto.Employee;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    public static String getNextTaxId() throws SQLException {
        String sql = "SELECT MAX(employee_id) FROM employee";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "E0001";
        }
        return "E" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    public static List<Employee> getEmployees(int i) throws SQLException {
        String sql = "SELECT * FROM employee WHERE status = ?";
        List<Employee> employees = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql, i);
        while (resultSet.next()) {
            employees.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getInt(7)
            ));
        }
        return employees;
    }

    public static boolean deleteEmployee(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static boolean addNewEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee(employee_id, username, password, email, document_type, document_number)VALUES (?,?,?,?,?,?)";
        return CrudUtil.execute(sql, employee.getId(), employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getDocumentType(), employee.getDocumentNumber());
    }

    public static boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET username = ?, password = ?, email = ?, document_type = ?, document_number = ? ,status = ? WHERE employee_id = ?";
        return CrudUtil.execute(sql, employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getDocumentType(), employee.getDocumentNumber(), employee.getStatus(), employee.getId());
    }

    public static boolean deleteSelectedEmployees(List<String> ids) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    public static boolean approveEmployee(String id) throws SQLException {
        String sql = "UPDATE employee SET status = '1' WHERE employee_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static boolean updateUser(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET username = ?, password = ?, email = ? WHERE employee_id = ?";
        return CrudUtil.execute(sql, employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getEmail());
    }
}
