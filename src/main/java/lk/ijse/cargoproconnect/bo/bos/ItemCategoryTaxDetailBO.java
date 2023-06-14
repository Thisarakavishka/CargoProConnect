package lk.ijse.cargoproconnect.bo.bos;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.dto.ItemCategoryTaxDetailsDTO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemCategoryTaxDetailBO {

    boolean addItemCategoryTaxDetail(ItemCategoryTaxDetailsDTO dto) throws SQLException, ClassNotFoundException;

    boolean delete(String id) throws SQLException, ClassNotFoundException;

    boolean deleteCategoryTaxDetails(ArrayList<String> ids) throws SQLException;

    boolean removeTax(String id, ArrayList<TaxDTO> removedTaxes) throws SQLException;

    int getTaxCount(String categoryId) throws SQLException;

    ArrayList<String> getTaxIds(String categoryId) throws SQLException;

    boolean addNewCategoryTaxDetails(String id, ObservableList<CategoryTaxTM> observableList) throws SQLException, ClassNotFoundException;

    boolean addTax(String id, ArrayList<TaxDTO> addedTaxes) throws SQLException, ClassNotFoundException;

}
