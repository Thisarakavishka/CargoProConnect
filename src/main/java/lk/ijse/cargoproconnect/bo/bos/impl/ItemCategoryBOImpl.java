package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.ItemCategoryBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.ItemCategoryDAO;
import lk.ijse.cargoproconnect.dto.CategoryDTO;
import lk.ijse.cargoproconnect.entity.ItemCategory;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCategoryBOImpl implements ItemCategoryBO {

    //Dependency Injection (Property Injection)
    ItemCategoryDAO itemCategoryDAO = (ItemCategoryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM_CATEGORY);

    @Override
    public ArrayList<CategoryDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
        ArrayList<ItemCategory> itemCategories = itemCategoryDAO.getAll();
        for (ItemCategory itemCategory : itemCategories) {
            categoryDTOS.add(new CategoryDTO(itemCategory.getItemCategoryId(), itemCategory.getCategoryName()));
        }
        return categoryDTOS;
    }

    @Override
    public boolean addCustomer(CategoryDTO dto) throws SQLException, ClassNotFoundException {
        return itemCategoryDAO.add(new ItemCategory(dto.getId(), dto.getName()));
    }

    @Override
    public CategoryDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        ItemCategory itemCategory = itemCategoryDAO.search(id);
        return new CategoryDTO(itemCategory.getItemCategoryId(), itemCategory.getCategoryName());
    }

    @Override
    public boolean updateCustomer(CategoryDTO dto) throws SQLException, ClassNotFoundException {
        return itemCategoryDAO.update(new ItemCategory(dto.getId(), dto.getName()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return itemCategoryDAO.delete(id);
    }

    @Override
    public String generateNewCustomerId() throws SQLException, ClassNotFoundException {
        return itemCategoryDAO.generateNewId();
    }

    @Override
    public boolean deleteSelectedCategories(ArrayList<String> ids) throws SQLException {
        return itemCategoryDAO.deleteSelectedCategories(ids);
    }

    @Override
    public String getCategoryId(String categoryText) throws SQLException {
        return itemCategoryDAO.getCategoryId(categoryText);
    }
}
