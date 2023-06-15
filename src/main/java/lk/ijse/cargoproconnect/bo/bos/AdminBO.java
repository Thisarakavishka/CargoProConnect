package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.AdminDTO;

import java.sql.SQLException;

public interface AdminBO extends SuperBO {

    AdminDTO searchAdmin(String userName) throws SQLException, ClassNotFoundException;

    boolean searchAdmin(String userName , String password) throws SQLException;
}
