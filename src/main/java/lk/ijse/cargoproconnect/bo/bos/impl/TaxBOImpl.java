package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.TaxBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.ItemCategoryTaxDetailDAO;
import lk.ijse.cargoproconnect.dao.daos.TaxDAO;
import lk.ijse.cargoproconnect.db.DBConnection;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.entity.Tax;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaxBOImpl implements TaxBO {

    //Dependency Injection (Property Injection)
    TaxDAO taxDAO = (TaxDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.TAX);
    ItemCategoryTaxDetailDAO itemCategoryTaxDetailDAO = (ItemCategoryTaxDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM_CATEGORY_TAX_DETAIL);

    @Override
    public ArrayList<TaxDTO> getAllTaxes() throws SQLException, ClassNotFoundException {
        ArrayList<TaxDTO> taxDTOS = new ArrayList<>();
        ArrayList<Tax> taxes = taxDAO.getAll();
        for (Tax tax : taxes) {
            taxDTOS.add(new TaxDTO(tax.getId(), tax.getName(), tax.getPercentage(), tax.getDescription()));
        }
        return taxDTOS;
    }

    @Override
    public boolean addTax(TaxDTO dto) throws SQLException, ClassNotFoundException {
        return taxDAO.add(new Tax(dto.getId(), dto.getName(), dto.getPercentage(), dto.getDescription()));
    }

    @Override
    public TaxDTO searchTax(String id) throws SQLException, ClassNotFoundException {
        Tax tax = taxDAO.search(id);
        return new TaxDTO(tax.getId(), tax.getName(), tax.getPercentage(), tax.getDescription());
    }

    @Override
    public boolean updateTax(TaxDTO dto) throws SQLException, ClassNotFoundException {
        return taxDAO.update(new Tax(dto.getId(), dto.getName(), dto.getPercentage(), dto.getDescription()));
    }

    @Override
    public boolean deleteTax(String id) throws SQLException, ClassNotFoundException {
        return taxDAO.delete(id);
    }

    @Override
    public String generateNewTaxId() throws SQLException, ClassNotFoundException {
        return taxDAO.generateNewId();
    }

    @Override
    public ArrayList<TaxDTO> getAllTaxes(ArrayList<String> ids) throws SQLException {
        ArrayList<TaxDTO> taxDTOS = new ArrayList<>();
        ArrayList<Tax> taxes = taxDAO.getAll(ids);
        for (Tax tax : taxes) {
            taxDTOS.add(new TaxDTO(tax.getId(), tax.getName(), tax.getPercentage(), tax.getDescription()));
        }
        return taxDTOS;
    }

    @Override
    public boolean deleteSelectedTaxes(ArrayList<String> ids) throws SQLException {
        return taxDAO.deleteSelectedTaxes(ids);
    }

    @Override
    public double getTotalTax(String categoryId, String price, String qty) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<Double> percentage = new ArrayList<>();
            ArrayList<String> taxIds = itemCategoryTaxDetailDAO.getTaxIds(categoryId);

            if (!taxIds.isEmpty()) {
                for (String id : taxIds) {
                    Tax tax = taxDAO.search(id);
                    percentage.add(tax.getPercentage());
                }
            }
            Double totalTax = Double.valueOf(0);
            for (Double value : percentage) {
                totalTax += Double.parseDouble(price) * (value / 100.0);
            }
            return totalTax * Integer.parseInt(qty);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
            return 0;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
