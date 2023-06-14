package lk.ijse.cargoproconnect.bo.bos.impl;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.bos.ItemCategoryTaxDetailBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.ItemCategoryTaxDetailDAO;
import lk.ijse.cargoproconnect.dto.ItemCategoryTaxDetailsDTO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;
import lk.ijse.cargoproconnect.entity.ItemCategoryTaxDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCategoryTaxDetailBOImpl implements ItemCategoryTaxDetailBO {

    //Dependency Injection (Property Injection)
    ItemCategoryTaxDetailDAO itemCategoryTaxDetailDAO = (ItemCategoryTaxDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM_CATEGORY_TAX_DETAIL);

    @Override
    public boolean addItemCategoryTaxDetail(ItemCategoryTaxDetailsDTO dto) throws SQLException, ClassNotFoundException {
        return itemCategoryTaxDetailDAO.add(new ItemCategoryTaxDetails(dto.getItemCategoryId(), dto.getTaxId()));
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return itemCategoryTaxDetailDAO.delete(id);
    }

    @Override
    public boolean deleteCategoryTaxDetails(ArrayList<String> ids) throws SQLException {
        return itemCategoryTaxDetailDAO.deleteCategoryTaxDetails(ids);
    }

    @Override
    public boolean removeTax(String id, ArrayList<TaxDTO> removedTaxes) throws SQLException {
        return itemCategoryTaxDetailDAO.removeTax(id, removedTaxes);
    }

    @Override
    public int getTaxCount(String categoryId) throws SQLException {
        return itemCategoryTaxDetailDAO.getTaxCount(categoryId);
    }

    @Override
    public ArrayList<String> getTaxIds(String categoryId) throws SQLException {
        return itemCategoryTaxDetailDAO.getTaxIds(categoryId);
    }

    @Override
    public boolean addNewCategoryTaxDetails(String id, ObservableList<CategoryTaxTM> observableList) throws SQLException, ClassNotFoundException {
        for (CategoryTaxTM categoryTaxTM : observableList) {
            if (!itemCategoryTaxDetailDAO.add(new ItemCategoryTaxDetails(id, categoryTaxTM.getId()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addTax(String id, ArrayList<TaxDTO> addedTaxes) throws SQLException, ClassNotFoundException {
        if (addedTaxes.isEmpty()) {
            return true;
        }
        for (TaxDTO tax : addedTaxes) {
            boolean isSave = itemCategoryTaxDetailDAO.add(new ItemCategoryTaxDetails(id, tax.getId()));
            if (!isSave) {
                return false;
            }
        }
        return true;
    }
}
