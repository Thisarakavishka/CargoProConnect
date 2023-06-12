package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.DeliverDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DeliverDetailDAO extends CrudDAO<DeliverDetails> {

    ArrayList<DeliverDetails> getDeliveries(int i) throws SQLException;

    boolean confirmDeliver(String id, String employeeId) throws SQLException;
}
