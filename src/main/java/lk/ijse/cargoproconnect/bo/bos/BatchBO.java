package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.BatchDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BatchBO {

    public ArrayList<BatchDTO> getAllBatches() throws SQLException, ClassNotFoundException;

    public boolean addBatch(BatchDTO dto) throws SQLException, ClassNotFoundException;

    public BatchDTO searchBatch(String id) throws SQLException, ClassNotFoundException;

    public boolean updateBatch(BatchDTO dto) throws SQLException, ClassNotFoundException;

    public boolean deleteBatch(String id) throws SQLException, ClassNotFoundException;

    public boolean existBatch(String id) throws SQLException, ClassNotFoundException;

    public String generateNewBatchId() throws SQLException, ClassNotFoundException;
}
