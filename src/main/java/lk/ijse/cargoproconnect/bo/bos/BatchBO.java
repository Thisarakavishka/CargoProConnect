package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.BatchDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BatchBO extends SuperBO {

    ArrayList<BatchDTO> getAllBatches() throws SQLException, ClassNotFoundException;

    boolean addBatch(BatchDTO dto) throws SQLException, ClassNotFoundException;

    BatchDTO searchBatch(String id) throws SQLException, ClassNotFoundException;

    boolean updateBatch(BatchDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteBatch(String id) throws SQLException, ClassNotFoundException;

    String generateNewBatchId() throws SQLException, ClassNotFoundException;

    boolean deleteSelectedBatches(ArrayList<String> ids) throws SQLException;

    ArrayList<BatchDTO> getAvailableBatches() throws SQLException;

    boolean updateBatchWeight(String batchId, int weight) throws SQLException;

    void setBatchStatus() throws SQLException;

    void setAvailableBatchStatus(int i, String batchId) throws SQLException;

    boolean setCurrentBatchOrders(String batchId, int i) throws SQLException;
}
