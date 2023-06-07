package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.ItemCategory;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemCategoryDAO extends CrudDAO<ItemCategory> {

    boolean deleteSelectedCategories(ArrayList<String> ids) throws SQLException;

    String getCategoryId(String categoryText) throws SQLException;
}