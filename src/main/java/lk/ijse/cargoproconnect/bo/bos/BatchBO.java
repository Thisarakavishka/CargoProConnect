package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.entity.Batch;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BatchBO {

//    public ArrayList<BatchDTO> getAllBatches() throws SQLException, ClassNotFoundException;
//
//    public boolean addBatch(BatchDTO dto) throws SQLException, ClassNotFoundException;
//
//    public BatchDTO searchBatch(String id) throws SQLException, ClassNotFoundException;
//
//    public boolean updateBatch(BatchDTO dto) throws SQLException, ClassNotFoundException;
//
//    public boolean deleteBatch(String id) throws SQLException, ClassNotFoundException;
//
//    public String generateNewBatchId() throws SQLException, ClassNotFoundException;

    public ArrayList<BatchDTO> getAllBatches() throws SQLException, ClassNotFoundException;

    public boolean add(BatchDTO dto) throws SQLException, ClassNotFoundException;

    public BatchDTO search(String id) throws SQLException, ClassNotFoundException;

    public boolean update(BatchDTO dto) throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public String generateNewId() throws SQLException, ClassNotFoundException;

    public boolean deleteSelectedBatches(ArrayList<String> ids) throws SQLException;

    public ArrayList<BatchDTO> getAvailableBatches() throws SQLException;

    public boolean updateBatchWeight(String batchId, int weight) throws SQLException;

    public void setStatus() throws SQLException;

    public void setAvailableStatus(int i, String batchId) throws SQLException;

    public boolean setCurrentOrders(String batchId, int i) throws SQLException;
}
