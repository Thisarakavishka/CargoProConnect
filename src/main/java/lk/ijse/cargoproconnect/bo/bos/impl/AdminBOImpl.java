package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.AdminBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.AdminDAO;
import lk.ijse.cargoproconnect.dto.AdminDTO;
import lk.ijse.cargoproconnect.entity.Admin;

import java.sql.SQLException;

public class AdminBOImpl implements AdminBO {

    //Dependency Injection (Property Injection)
    AdminDAO adminDAO = (AdminDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ADMIN);

    @Override
    public AdminDTO searchAdmin(String userName) throws SQLException, ClassNotFoundException {
        Admin admin = adminDAO.search(userName);
        return new AdminDTO(admin.getId(), admin.getUsername(), admin.getPassword(), admin.getEmail());
    }

    @Override
    public boolean searchAdmin(String username, String password) throws SQLException {
        return adminDAO.searchAdmin(username, password);
    }
}
