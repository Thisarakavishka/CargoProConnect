package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.AdminDAO;
import lk.ijse.cargoproconnect.entity.Admin;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public ArrayList<Admin> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean add(Admin dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public Admin search(String userName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM admin WHERE username = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userName);
        if (resultSet.next()) {
            return new Admin(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
        }
        return null;
    }

    @Override
    public boolean update(Admin dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean searchAdmin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, username, password);
        if (resultSet.next()) {
            if (resultSet.getString(3).equalsIgnoreCase(username) && resultSet.getString(2).equalsIgnoreCase(password)) {
                return true;
            }
        }
        return false;
    }
}
