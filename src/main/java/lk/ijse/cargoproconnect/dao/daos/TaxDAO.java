package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.Tax;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TaxDAO extends CrudDAO<Tax> {

    boolean deleteSelectedTaxes(ArrayList<String> ids) throws SQLException;
}
