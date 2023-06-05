package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.entity.Batch;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BatchBO {

    public ArrayList<Batch> getAllBatches() throws SQLException, ClassNotFoundException;

    public boolean addBatch(Batch dto) throws SQLException, ClassNotFoundException;

    public Batch searchBatch(String id) throws SQLException, ClassNotFoundException;

    public boolean updateBatch(Batch dto) throws SQLException, ClassNotFoundException;

    public boolean deleteBatch(String id) throws SQLException, ClassNotFoundException;

    public boolean existBatch(String id) throws SQLException, ClassNotFoundException;

    public String generateNewBatchId() throws SQLException, ClassNotFoundException;
}
