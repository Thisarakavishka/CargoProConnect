package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.BatchDAO;
import lk.ijse.cargoproconnect.entity.Batch;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BatchDAOImpl implements BatchDAO {

    @Override
    public ArrayList<Batch> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM batch");
        ArrayList<Batch> batches = new ArrayList<>();
        while (resultSet.next()) {
            batches.add(new Batch(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9)));
        }
        return batches;
    }

    @Override
    public boolean add(Batch dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO batch (batch_id, shipment_date, delivery_date, total_weight, delivery_address, shipment_type) VALUES (?, ?, ?, ?, ?, ?)", dto.getId(), dto.getSDate(), dto.getDDate(), dto.getTotalWeight(), dto.getDeliveryAddress(), dto.getShipmentType());
    }

    @Override
    public Batch search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM batch WHERE batch_id = ?", id);
        if (resultSet.next()) {
            return new Batch(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9));
        }
        return null;
    }

    @Override
    public boolean update(Batch dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE batch SET shipment_date = ?, delivery_date = ?, total_weight = ?, delivery_address = ?, shipment_type = ? WHERE batch_id =?", dto.getSDate(), dto.getDDate(), dto.getTotalWeight(), dto.getDeliveryAddress(), dto.getShipmentType(), dto.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM batch WHERE batch_id = ?", id);
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(batch_id) FROM batch");

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "B0001";
        }
        return "B" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }
}
