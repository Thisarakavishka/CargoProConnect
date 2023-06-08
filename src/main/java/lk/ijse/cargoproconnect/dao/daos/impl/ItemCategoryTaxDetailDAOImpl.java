package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.ItemCategoryTaxDetailDAO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.entity.ItemCategoryTaxDetails;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCategoryTaxDetailDAOImpl implements ItemCategoryTaxDetailDAO {
    @Override
    public ArrayList<ItemCategoryTaxDetails> getAll() {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean add(ItemCategoryTaxDetails dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO item_category_tax_details(item_category_id, tax_id) VALUES (?, ?)", dto.getItemCategoryId(), dto.getTaxId());
    }

    @Override
    public ItemCategoryTaxDetails search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean update(ItemCategoryTaxDetails dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item_category_tax_details WHERE item_category_id = ?", id);
        if(resultSet.next()){
            return CrudUtil.execute("DELETE FROM item_category_tax_details WHERE item_category_id = ?", id);
        }else {
            return true;
        }
    }

    @Override
    public String generateNewId() {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean deleteCategoryTaxDetails(ArrayList<String> ids) throws SQLException {
        String sql = "DELETE FROM item_category_tax_details WHERE item_category_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    @Override
    public boolean removeTax(String id, ArrayList<TaxDTO> removedTaxes) throws SQLException {
        if (removedTaxes ==null || id == null || id.isEmpty()) {
            return true;
        }
        for (TaxDTO tax : removedTaxes) {
            boolean isRemoved = CrudUtil.execute("DELETE FROM item_category_tax_details WHERE item_category_id = ? AND tax_id = ?", id, tax.getId());
            if(!isRemoved){
                return false;
            }
        }
        return true;
    }

    @Override
    public int getTaxCount(String categoryId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item_category_tax_details WHERE item_category_id = ?", categoryId);
        int count = 0;
        while (resultSet.next()) {
            count += 1;
        }
        return count;
    }

    @Override
    public ArrayList<String> getTaxIds(String categoryId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT tax_id FROM item_category_tax_details WHERE item_category_id = ?", categoryId);
        ArrayList<String> taxIds = new ArrayList<>();
        while (resultSet.next()) {
            taxIds.add(resultSet.getString(1));
        }
        return taxIds;
    }

     //**********************************************************************************************************************************
    // i need to implement these methods in business layer

    //    public static boolean addNewCategoryTaxDetails(String id, ObservableList<CategoryTaxTM> observableList) throws SQLException {
    //        for (CategoryTaxTM categoryTaxTM : observableList) {
    //            if (!isSave(id, categoryTaxTM.getId())) {
    //                return false;
    //            }
    //        }
    //        return true;
    //    }

    //    public static boolean addTax(String id, List<TaxDTO> addedTaxes) throws SQLException {
    //        if (addedTaxes.isEmpty()) {
    //            return true;
    //        }
    //        for (TaxDTO tax : addedTaxes) {
    //            boolean isSave = isSave(id , tax.getId());
    //            if(!isSave){
    //                return false;
    //            }
    //        }
    //        return true;
    //    }

    //use addItemCategoryTaxDetail() for isSave() method in business layer

    //**********************************************************************************************************************************
}
