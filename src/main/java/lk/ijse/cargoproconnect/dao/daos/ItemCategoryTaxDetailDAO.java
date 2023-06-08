package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.entity.ItemCategoryTaxDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemCategoryTaxDetailDAO extends CrudDAO<ItemCategoryTaxDetails> {

    boolean deleteCategoryTaxDetails(ArrayList<String> ids) throws SQLException;

    boolean removeTax(String id, ArrayList<TaxDTO> removedTaxes) throws SQLException;

    int getTaxCount(String categoryId) throws SQLException;

    ArrayList<String> getTaxIds(String categoryId) throws SQLException;

}
