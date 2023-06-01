package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.dto.AdminDTO;
import lk.ijse.cargoproconnect.dto.EmployeeDTO;
import lk.ijse.cargoproconnect.util.CrudUtil;
import lk.ijse.cargoproconnect.util.SecurityUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    private static EmployeeDTO employee;
    private static AdminDTO admin;

    public static boolean searchAdmin(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userName, password);
        if (resultSet.next()) {
            if (resultSet.getString(3).equalsIgnoreCase(password) && resultSet.getString(2).equalsIgnoreCase(userName)) {
                admin = new AdminDTO(resultSet.getString(1),resultSet.getString(2),resultSet.getString(4));
                return true;
            }
        }
        return false;
    }

    public static boolean searchEmployee(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM employee WHERE username = ? AND password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userName, password);
        if (resultSet.next()) {
            if (resultSet.getString(2).equalsIgnoreCase(userName) && resultSet.getString(3).equalsIgnoreCase(password)) {
              // set id , decoded username and email
                employee = new EmployeeDTO(resultSet.getString(1), SecurityUtil.decoder(resultSet.getString(2)), resultSet.getString(4));
                return true;
            }
        }
        return false;
    }

    public static boolean searchStatus(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM employee WHERE username = ? AND password = ? AND status = 1";
        ResultSet resultSet = CrudUtil.execute(sql, userName, password);
        if (resultSet.next()) {
            return true;
        }
        return false;
    }

    public static String getAdminId() {
        return admin.getId();
    }

    public static String getEmployeeId() {
        return employee.getId();
    }

    public static String getAdminUserName() {
        return admin.getUsername();
    }

    public static String getEmployeeUserName() {
        return employee.getUsername();
    }

    public static String getAdminEmail() {
        return admin.getEmail();
    }

    public static String getEmployeeEmail() {
        return employee.getEmail();
    }
}
