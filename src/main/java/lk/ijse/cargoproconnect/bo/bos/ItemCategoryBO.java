package lk.ijse.cargoproconnect.bo.bos;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.CategoryDTO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemCategoryBO extends SuperBO {

    ArrayList<CategoryDTO> getAllCategories() throws SQLException, ClassNotFoundException;

    boolean addNewCategory(String id, String name, ObservableList<CategoryTaxTM> observableList) throws SQLException;

    CategoryDTO searchCategory(String id) throws SQLException, ClassNotFoundException;

    boolean updateCategory(String id, String name, ArrayList<TaxDTO> addedTaxes, ArrayList<TaxDTO> removedTaxes) throws SQLException;

    boolean deleteCategory(String id) throws SQLException, ClassNotFoundException;

    String generateNewCategoryId() throws SQLException, ClassNotFoundException;

    boolean deleteSelectedCategories(ArrayList<String> ids) throws SQLException;

    String getCategoryId(String categoryText) throws SQLException;

    ArrayList<TaxDTO> getIncludedTaxes(String id) throws SQLException;

    int taxCount(String categoryText) throws SQLException;
}
