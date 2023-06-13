package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.CategoryDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemCategoryBO {

    ArrayList<CategoryDTO> getAllCustomers() throws SQLException, ClassNotFoundException;

    boolean addCustomer(CategoryDTO dto) throws SQLException, ClassNotFoundException;

    CategoryDTO searchCustomer(String id) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(CategoryDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;

    String generateNewCustomerId() throws SQLException, ClassNotFoundException;

    boolean deleteSelectedCategories(ArrayList<String> ids) throws SQLException;

    String getCategoryId(String categoryText) throws SQLException;
}
