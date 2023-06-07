package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.ItemCategoryDAO;
import lk.ijse.cargoproconnect.entity.ItemCategory;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCategoryDAOImpl implements ItemCategoryDAO {

    @Override
    public ArrayList<ItemCategory> getAll() throws SQLException {
        ArrayList<ItemCategory> categories = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item_category");
        while (resultSet.next()) {
            categories.add(new ItemCategory(resultSet.getString(1), resultSet.getString(2)));
        }
        return categories;
    }

    @Override
    public boolean add(ItemCategory dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO item_category(item_category_id, category_name) VALUES (?, ?)";
        return CrudUtil.execute(sql, dto.getItemCategoryId(), dto.getCategoryName());
    }

    @Override
    public ItemCategory search(String id) throws SQLException, ClassNotFoundException {
        return null;     // not implement this method yet !
    }

    @Override
    public boolean update(ItemCategory dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item_category SET category_name = ? WHERE Item_category_id = ?";
        return CrudUtil.execute(sql, dto.getCategoryName(), dto.getItemCategoryId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item_category WHERE item_category_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(item_category_id) FROM item_category");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "IC0001";
        }
        return "IC" + String.format("%04d", Integer.parseInt(currentMaxId.substring(2)) + 1);
    }

    @Override
    public boolean deleteSelectedCategories(ArrayList<String> ids) throws SQLException {
        String sql = "DELETE FROM item_category WHERE item_category_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    @Override
    public String getCategoryId(String categoryText) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item_category WHERE category_name = ?", categoryText);
        String id = "";
        if(resultSet.next()){
            id = resultSet.getString(1);
        }
        return id;
    }
}
