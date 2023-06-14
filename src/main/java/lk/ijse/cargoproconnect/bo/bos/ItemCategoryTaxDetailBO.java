package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.ItemCategoryTaxDetailsDTO;
import lk.ijse.cargoproconnect.dto.TaxDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemCategoryTaxDetailBO extends SuperBO {

    boolean addItemCategoryTaxDetail(ItemCategoryTaxDetailsDTO dto) throws SQLException, ClassNotFoundException;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    boolean deleteCategoryTaxDetails(ArrayList<String> ids) throws SQLException;

    boolean removeTax(String id, ArrayList<TaxDTO> removedTaxes) throws SQLException;

    int getTaxCount(String categoryId) throws SQLException;

    ArrayList<String> getTaxIds(String categoryId) throws SQLException;

}
