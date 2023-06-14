package lk.ijse.cargoproconnect.bo.bos.impl;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.bos.ItemCategoryBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.ItemCategoryDAO;
import lk.ijse.cargoproconnect.dao.daos.ItemCategoryTaxDetailDAO;
import lk.ijse.cargoproconnect.dao.daos.TaxDAO;
import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.CategoryDTO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;
import lk.ijse.cargoproconnect.entity.ItemCategory;
import lk.ijse.cargoproconnect.entity.Tax;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCategoryBOImpl implements ItemCategoryBO {

    //Dependency Injection (Property Injection)
    ItemCategoryDAO itemCategoryDAO = (ItemCategoryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM_CATEGORY);
    ItemCategoryTaxDetailDAO itemCategoryTaxDetailDAO = (ItemCategoryTaxDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM_CATEGORY_TAX_DETAIL);
    TaxDAO taxDAO = (TaxDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.TAX);

    @Override
    public ArrayList<CategoryDTO> getAllCategories() throws SQLException, ClassNotFoundException {
        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
        ArrayList<ItemCategory> itemCategories = itemCategoryDAO.getAll();
        for (ItemCategory itemCategory : itemCategories) {
            categoryDTOS.add(new CategoryDTO(itemCategory.getItemCategoryId(), itemCategory.getCategoryName()));
        }
        return categoryDTOS;
    }

    @Override
    public CategoryDTO searchCategory(String id) throws SQLException, ClassNotFoundException {
        ItemCategory itemCategory = itemCategoryDAO.search(id);
        return new CategoryDTO(itemCategory.getItemCategoryId(), itemCategory.getCategoryName());
    }

    @Override
    public String generateNewCategoryId() throws SQLException, ClassNotFoundException {
        return itemCategoryDAO.generateNewId();
    }

    @Override
    public String getCategoryId(String categoryText) throws SQLException {
        return itemCategoryDAO.getCategoryId(categoryText);
    }

    //Transaction
    @Override
    public boolean deleteCategory(String id) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isDeleted = itemCategoryTaxDetailDAO.delete(id);
            if (isDeleted) {
                boolean isDeleteDetails = itemCategoryDAO.delete(id);
                if (isDeleteDetails) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //Transaction
    @Override
    public boolean deleteSelectedCategories(ArrayList<String> ids) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isDeleted = itemCategoryTaxDetailDAO.deleteCategoryTaxDetails(ids);
            if (isDeleted) {
                boolean isDeleteDetails = itemCategoryDAO.deleteSelectedCategories(ids);
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

    //Transaction
    @Override
    public boolean addNewCategory(String id, String name, ObservableList<CategoryTaxTM> observableList) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isAdded = itemCategoryDAO.add(new ItemCategory(id, name));
            if (isAdded) {
                boolean isAddDetails = itemCategoryTaxDetailDAO.addNewCategoryTaxDetails(id, observableList);
                if (isAddDetails) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //Transaction
    @Override
    public boolean updateCategory(String id, String name, ArrayList<TaxDTO> addedTaxes, ArrayList<TaxDTO> removedTaxes) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isUpdate = itemCategoryDAO.update(new ItemCategory(id, name));
            boolean isAdd = itemCategoryTaxDetailDAO.addTax(id, addedTaxes);
            boolean isRemove = itemCategoryTaxDetailDAO.removeTax(id, removedTaxes);

            if (isAdd || isUpdate || isRemove) {
                connection.commit();
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //Transaction
    @Override
    public ArrayList<TaxDTO> getIncludedTaxes(String id) throws SQLException {
        Connection connection = null;
        ArrayList<Tax> taxes = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<String> ids;
            ids = itemCategoryTaxDetailDAO.getTaxIds(id);
            if (!ids.isEmpty()) {
                taxes = taxDAO.getAll(ids);
                if (!taxes.isEmpty()) {
                    ArrayList<TaxDTO> taxDTOS = new ArrayList<>();
                    for (Tax tax : taxes) {
                        taxDTOS.add(new TaxDTO(tax.getId(), tax.getName(), tax.getPercentage(), tax.getDescription()));
                    }
                    connection.commit();
                    return taxDTOS;
                }
            } else if (ids.isEmpty()) {
                return null;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return null;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //Transaction without commit
    @Override
    public int taxCount(String categoryText) throws SQLException {
        String categoryId = itemCategoryDAO.getCategoryId(categoryText);
        return itemCategoryTaxDetailDAO.getTaxCount(categoryId);
    }
}
