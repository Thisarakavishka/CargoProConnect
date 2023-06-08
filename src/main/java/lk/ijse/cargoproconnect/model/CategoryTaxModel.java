package lk.ijse.cargoproconnect.model;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.CategoryDTO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryTaxModel {

    public static Connection connection = null;

    public static boolean deleteCategory(String id) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isDeleted = CategoryModel.deleteCategoryTaxDetails(id);
            if (isDeleted) {
                boolean isDeleteDetails = CategoryModel.deleteCategory(id);
                if (isDeleteDetails) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static boolean deleteSelectedCategories(List<String> ids) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isDeleted = CategoryModel.deleteCategoryTaxDetails(ids);
            if (isDeleted) {
                boolean isDeleteDetails = CategoryModel.deleteCategory(ids);
                if (isDeleteDetails) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static boolean addNewCategory(String id, String name, ObservableList<CategoryTaxTM> observableList) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isAdded = CategoryModel.addNewCategory(new CategoryDTO(id, name));
            if (isAdded) {
                boolean isAddDetails = CategoryModel.addNewCategoryTaxDetails(id, observableList);
                if (isAddDetails) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static List<TaxDTO> getIncludedTaxes(String id) throws SQLException {
        List<TaxDTO> taxes = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            List<String> ids = new ArrayList<>();
            ids = TaxModel.getTaxIds(id);
            if (!ids.isEmpty()) {
                taxes = CategoryModel.getTaxes(ids);
                if (!taxes.isEmpty()) {
                    connection.commit();
                    return taxes;
                }
            } else if (ids.isEmpty()) {
                return null;
            }
            return taxes;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return taxes;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static boolean updateCategory(String id, String name, List<TaxDTO> addedTaxes, List<TaxDTO> removedTaxes) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isUpdate = CategoryModel.updateCategoryName(new CategoryDTO(id, name));
            boolean isAdd = CategoryModel.addTax(id, addedTaxes);
            boolean isRemove = CategoryModel.removeTax(id, removedTaxes);

            if(isAdd || isUpdate || isRemove){
                connection.commit();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static int taxCount(String categoryText) throws SQLException {
        String categoryId = CategoryModel.getCategoryId(categoryText);
        int count = TaxModel.getTaxCount(categoryId);
        return count;
    }
}
