package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.TaxDAO;
import lk.ijse.cargoproconnect.entity.Tax;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaxDAOImpl implements TaxDAO {

    @Override
    public ArrayList<Tax> getAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM tax");
        ArrayList<Tax> taxes = new ArrayList<>();
        while (resultSet.next()) {
            taxes.add(new Tax(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getString(4)));
        }
        return taxes;
    }

    @Override
    public boolean add(Tax dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO tax(tax_id, tax_name, tax_percentage, tax_description) VALUES(?,?,?,?)", dto.getId(), dto.getName(), dto.getPercentage(), dto.getDescription());
    }

    @Override
    public Tax search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM tax WHERE tax_id=?", id);
        resultSet.next();
        return new Tax(resultSet.getString(1),resultSet.getString(2),resultSet.getDouble(3),resultSet.getString(4) );
    }

    @Override
    public boolean update(Tax dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE tax SET tax_name = ?, tax_percentage = ?, tax_description = ? WHERE tax_id = ?", dto.getName(), dto.getPercentage(), dto.getDescription(), dto.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM tax WHERE tax_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(tax_id) FROM tax");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "T0001";
        }
        return "T" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    @Override
    public boolean deleteSelectedTaxes(ArrayList<String> ids) throws SQLException {
        String sql = "DELETE FROM tax WHERE tax_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }
}
