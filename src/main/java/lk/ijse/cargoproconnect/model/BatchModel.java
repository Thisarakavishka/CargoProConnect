package lk.ijse.cargoproconnect.model;

import lk.ijse.cargoproconnect.dto.Batch;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatchModel {

    public static String getNextTaxId() throws SQLException {
        String sql = "SELECT MAX(batch_id) FROM batch";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "B0001";
        }
        return "B" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

    public static List<Batch> getBatches() throws SQLException {
        String sql = "SELECT * FROM batch";
        List<Batch> batches = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            batches.add(new Batch(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    resultSet.getInt(8),
                    resultSet.getString(9)
            ));
        }
        return batches;
    }

    public static boolean addNewBatch(Batch batch) throws SQLException {
        String sql = "INSERT INTO batch (batch_id, shipment_date, delivery_date, total_weight, delivery_address, shipment_type) VALUES (?, ?, ?,? , ?, ?)";
        return CrudUtil.execute(sql, batch.getId(), batch.getDDate(), batch.getDDate(), batch.getTotalWeight(), batch.getDeliveryAddress(), batch.getShipmentType());
    }

    public static boolean deleteBatch(String id) throws SQLException {
        String sql = "DELETE FROM batch WHERE batch_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static boolean updateBatch(Batch batch) throws SQLException {
        String sql = "UPDATE batch SET shipment_date = ?, delivery_date = ?, total_weight = ?, delivery_address = ?, shipment_type = ? WHERE batch_id =?";
        return CrudUtil.execute(sql, batch.getSDate(), batch.getDDate(), batch.getTotalWeight(), batch.getDeliveryAddress(), batch.getShipmentType(), batch.getId());
    }

    public static boolean deleteSelectedBatches(List<String> ids) throws SQLException {
        String sql = "DELETE FROM batch WHERE batch_id IN (";
        for (String id : ids) {
            sql += "'" + id + "',";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return CrudUtil.execute(sql);
    }

    public static List<Batch> getAvailableBatches() throws SQLException {
        String sql = "SELECT * FROM batch WHERE available_status = 1";
        List<Batch> batches = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            batches.add(new Batch(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    resultSet.getInt(8),
                    resultSet.getString(9)
            ));
        }
        return batches;
    }

    public static String getDeliverDate(String batchId) throws SQLException {
        String sql = "SELECT delivery_date FROM batch WHERE batch_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, batchId);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return resultSet.getString(1);
    }

    public static boolean updateBatchWeight(String batchId, int weight) throws SQLException {
        String sql = "UPDATE batch SET current_weight = ? WHERE batch_id = ?";
        return CrudUtil.execute(sql, weight, batchId);
    }

    public static int getCurrentWeight(String batchId) throws SQLException {
        String sql = "SELECT current_weight FROM batch WHERE batch_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, batchId);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getTotalWeight(String batchId) throws SQLException {
        String sql = "SELECT total_weight FROM batch WHERE batch_id = ?";
        try (ResultSet resultSet = CrudUtil.execute(sql, batchId)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public static void setStatus() throws SQLException {
        String sql = "SELECT batch_id, total_weight , current_weight , shipment_date FROM batch";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            LocalDate shipmentDate = LocalDate.parse(resultSet.getString(4));

            if (resultSet.getInt(2) == resultSet.getInt(3) || shipmentDate.isBefore(LocalDate.now())) {
                setAvailableStatus(0, resultSet.getString(1));
            }
        }
    }

    private static void setAvailableStatus(int i, String batchId) throws SQLException {
        String sql = "UPDATE batch SET available_status = ? WHERE batch_id = ?";
        CrudUtil.execute(sql, i, batchId);
    }

    public static int getCurrentOrders(String batchId) throws SQLException {
        String sql = "SELECT number_of_orders FROM batch WHERE batch_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, batchId);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static boolean setCurrentOrders(String batchId, int i) throws SQLException {
        String sql = "UPDATE batch SET number_of_orders = ? WHERE batch_id = ?";
        return CrudUtil.execute(sql, i, batchId);
    }

    public static Batch getBatch(String batchId) throws SQLException {
        String sql = "SELECT * FROM batch WHERE batch_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, batchId);
        if (resultSet.next()) {
            return new Batch(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getInt(6),
                    resultSet.getString(7),
                    resultSet.getInt(8),
                    resultSet.getString(9)
            );
        }
        return null;
    }
}
