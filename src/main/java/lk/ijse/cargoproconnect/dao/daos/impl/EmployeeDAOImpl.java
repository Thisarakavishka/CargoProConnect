package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.EmployeeDAO;
import lk.ijse.cargoproconnect.entity.Employee;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public ArrayList<Employee> getAll() {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean add(Employee dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO employee(employee_id, username, password, email, document_type, document_number)VALUES (?,?,?,?,?,?)", dto.getId(), dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getDocumentType(), dto.getDocumentNumber());
    }

    @Override
    public Employee search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean update(Employee dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE employee SET username = ?, password = ?, email = ?, document_type = ?, document_number = ? ,status = ? WHERE employee_id = ?", dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getDocumentType(), dto.getDocumentNumber(), dto.getStatus(), dto.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM employee WHERE employee_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(employee_id) FROM employee");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "E0001";
        }
        return "E" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    @Override
    public ArrayList<Employee> getEmployees(int i) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee WHERE status = ?", i);
        while (resultSet.next()) {
            employees.add(new Employee(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7)));
        }
        return employees;
    }

    @Override
    public boolean deleteSelectedEmployees(ArrayList<String> ids) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    @Override
    public boolean approveEmployee(String id) throws SQLException {
        return CrudUtil.execute("UPDATE employee SET status = '1' WHERE employee_id = ?", id);
    }

    @Override
    public boolean updateUser(Employee employee) throws SQLException {
        return CrudUtil.execute("UPDATE employee SET username = ?, password = ?, email = ? WHERE employee_id = ?", employee.getUsername(), employee.getPassword(), employee.getEmail(), employee.getId());
    }
}
