package lk.ijse.cargoproconnect.dao.daos;

import lk.ijse.cargoproconnect.dao.CrudDAO;
import lk.ijse.cargoproconnect.entity.Batch;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BatchDAO extends CrudDAO<Batch> {

    public boolean deleteSelectedBatches(ArrayList<String> ids) throws SQLException ;

    public ArrayList<Batch> getAvailableBatches() throws SQLException ;

    public boolean updateBatchWeight(String batchId, int weight) throws SQLException;

    public void setStatus() throws SQLException;

    public void setAvailableStatus(int i, String batchId) throws SQLException ;

    public boolean setCurrentOrders(String batchId, int i) throws SQLException ;

}
