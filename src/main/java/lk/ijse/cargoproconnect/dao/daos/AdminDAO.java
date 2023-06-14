package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.Admin;

import java.sql.SQLException;

public interface AdminDAO extends CrudDAO<Admin> {

    boolean searchAdmin(Admin admin) throws SQLException;
}
