package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.DeliverDetailDAO;
import lk.ijse.cargoproconnect.entity.DeliverDetails;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliverDetailDAOImpl implements DeliverDetailDAO {

    @Override
    public ArrayList<DeliverDetails> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM deliver_details";
        ArrayList<DeliverDetails> deliveries = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            deliveries.add(new DeliverDetails(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6)));
        }
        return deliveries;
    }

    @Override
    public boolean add(DeliverDetails dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO deliver_details(deliver_id, address, contact_1, contact_2) VALUES (?, ?, ?, ?)", dto.getId(), dto.getAddress(), dto.getContact1(), dto.getContact2());
    }

    @Override
    public DeliverDetails search(String deliverId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM  deliver_details WHERE deliver_id = ?", deliverId);
        if (resultSet.next()) {
            return new DeliverDetails(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6));
        }
        return null;
    }

    @Override
    public boolean update(DeliverDetails dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(deliver_id) FROM deliver_details");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "D0001";
        }
        return "D" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    @Override
    public ArrayList<DeliverDetails> getDeliveries(int i) throws SQLException {
        ArrayList<DeliverDetails> deliveries = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM deliver_details WHERE is_delivered = ?", i);
        while (resultSet.next()) {
            deliveries.add(new DeliverDetails(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6)));
        }
        return deliveries;
    }

    @Override
    public boolean confirmDeliver(String deliverId, String employeeId) throws SQLException {
        return CrudUtil.execute("UPDATE deliver_details SET is_delivered = 1 , confirmed_by = ? WHERE deliver_id = ?", employeeId, deliverId);
    }
}
