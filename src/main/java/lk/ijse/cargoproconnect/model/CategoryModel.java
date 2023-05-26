package lk.ijse.cargoproconnect.model;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.dto.Category;
import lk.ijse.cargoproconnect.dto.Tax;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryModel {

    public static List<Category> getCategories() throws SQLException {
        String sql = "SELECT * FROM item_category";
        List<Category> categories = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            categories.add(new Category(
                    resultSet.getString(1),
                    resultSet.getString(2)
            ));
        }
        return categories;
    }

    public static boolean deleteCategory(String id) throws SQLException {
        String sql = "DELETE FROM item_category WHERE item_category_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static boolean deleteCategoryTaxDetails(String id) throws SQLException {
        String sql = "DELETE FROM item_category_tax_details WHERE item_category_id = ?";
        String query = "SELECT * FROM item_category_tax_details WHERE item_category_id = ?";
        ResultSet resultSet = CrudUtil.execute(query, id);
        if(resultSet.next()){
            return CrudUtil.execute(sql, id);
        }else {
            return true;
        }
    }

    public static boolean deleteCategory(List<String> ids) throws SQLException {
        String sql = "DELETE FROM item_category WHERE item_category_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    public static boolean deleteCategoryTaxDetails(List<String> ids) throws SQLException {
        String sql = "DELETE FROM item_category_tax_details WHERE item_category_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    public static String getNextCategoryId() throws SQLException {
        String sql = "SELECT MAX(item_category_id) FROM item_category";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "IC0001";
        }
        return "IC" + String.format("%04d", Integer.parseInt(currentMaxId.substring(2)) + 1);
    }

    public static boolean addNewCategory(Category category) throws SQLException {
        String sql = "INSERT INTO item_category(item_category_id, category_name) VALUES (?, ?)";
        return CrudUtil.execute(sql, category.getId(), category.getName());
    }

    public static boolean addNewCategoryTaxDetails(String id, ObservableList<CategoryTaxTM> observableList) throws SQLException {
        for (CategoryTaxTM categoryTaxTM : observableList) {
            if (!isSave(id, categoryTaxTM.getId())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSave(String id, String taxId) throws SQLException {
        String sql = "INSERT INTO item_category_tax_details(item_category_id, tax_id) VALUES (?, ?)";
        return CrudUtil.execute(sql, id, taxId);
    }

    public static List<String> getTaxIds(String id) throws SQLException {
        String sql = "SELECT * FROM item_category_tax_details WHERE item_category_id = ?";
        List<String> ids = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql, id);
        while (resultSet.next()) {
            ids.add(resultSet.getString(2));
        }
        return ids;
    }

    public static List<Tax> getTaxes(List<String> ids) throws SQLException {
        String sql = "SELECT * FROM tax WHERE  tax_id = ?";
        List<Tax> taxes = new ArrayList<>();
        for (String id : ids) {
            ResultSet resultSet = CrudUtil.execute(sql, id);
            while (resultSet.next()) {
                taxes.add(new Tax(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3),
                        resultSet.getString(4))
                );
            }

        }
        return taxes;
    }

    public static boolean updateCategoryName(Category category) throws SQLException {
        String sql = "UPDATE item_category SET category_name = ? WHERE Item_category_id = ?";
        return CrudUtil.execute(sql, category.getName(), category.getId());
    }

    public static boolean addTax(String id, List<Tax> addedTaxes) throws SQLException {
        if (addedTaxes.isEmpty()) {
            return true;
        }
        for (Tax tax : addedTaxes) {
            boolean isSave = isSave(id , tax.getId());
            if(!isSave){
                return false;
            }
        }
        return true;
    }

    public static boolean removeTax(String id, List<Tax> removedTaxes) throws SQLException {
        if (removedTaxes ==null || id == null || id.isEmpty()) {
            return true;
        }
        String sql = "DELETE FROM item_category_tax_details WHERE item_category_id = ? AND tax_id = ?";
        for (Tax tax : removedTaxes) {
            boolean isRemoved = CrudUtil.execute(sql, id, tax.getId());
            if(!isRemoved){
                return false;
            }
        }
        return true;
    }

    public static String getCategoryId(String categoryText) throws SQLException {
        String sql ="SELECT * FROM item_category WHERE category_name = ?";
        ResultSet resultSet = CrudUtil.execute(sql, categoryText);
        String id = "";
        if(resultSet.next()){
            id = resultSet.getString(1);
        }
        return id;
    }
}
