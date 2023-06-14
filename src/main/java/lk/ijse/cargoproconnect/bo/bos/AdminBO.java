package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.AdminDTO;

import java.sql.SQLException;

public interface AdminBO {

    AdminDTO searchAdmin(String userName) throws SQLException, ClassNotFoundException;

    boolean searchAdmin(AdminDTO admin) throws SQLException;
}
