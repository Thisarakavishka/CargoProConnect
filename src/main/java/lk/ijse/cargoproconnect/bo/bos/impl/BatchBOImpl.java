package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.BatchBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.BatchDAO;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.entity.Batch;

import java.sql.SQLException;
import java.util.ArrayList;

public class BatchBOImpl implements BatchBO {

    //Dependency Injection (Property Injection)
    BatchDAO batchDAO = (BatchDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BATCH);

    @Override
    public ArrayList<BatchDTO> getAllBatches() throws SQLException, ClassNotFoundException {
        ArrayList<Batch> batches = batchDAO.getAll();
        ArrayList<BatchDTO> batchDTOS = new ArrayList<>();
        for (Batch batch : batches) {
            batchDTOS.add(new BatchDTO(batch.getId(), batch.getSDate(), batch.getDDate(), batch.getAvailableStatus(), batch.getTotalWeight(), batch.getCurrentWeight(), batch.getDeliveryAddress(), batch.getNoOfOrders(), batch.getShipmentType()));
        }
        return batchDTOS;
    }

    @Override
    public boolean addBatch(BatchDTO dto) throws SQLException, ClassNotFoundException {
        return batchDAO.add(new Batch(dto.getId(), dto.getSDate(), dto.getDDate(), dto.getAvailableStatus(), dto.getTotalWeight(), dto.getCurrentWeight(), dto.getDeliveryAddress(), dto.getNoOfOrders(), dto.getShipmentType()));
    }

    @Override
    public BatchDTO searchBatch(String id) throws SQLException, ClassNotFoundException {
        Batch batch = batchDAO.search(id);
        return new BatchDTO(batch.getId(), batch.getSDate(), batch.getDDate(), batch.getAvailableStatus(), batch.getTotalWeight(), batch.getCurrentWeight(), batch.getDeliveryAddress(), batch.getNoOfOrders(), batch.getShipmentType());
    }

    @Override
    public boolean updateBatch(BatchDTO dto) throws SQLException, ClassNotFoundException {
        return batchDAO.update(new Batch(dto.getId(), dto.getSDate(), dto.getDDate(), dto.getAvailableStatus(), dto.getTotalWeight(), dto.getCurrentWeight(), dto.getDeliveryAddress(), dto.getNoOfOrders(), dto.getShipmentType()));
    }

    @Override
    public boolean deleteBatch(String id) throws SQLException, ClassNotFoundException {
        return batchDAO.delete(id);
    }

    @Override
    public String generateNewBatchId() throws SQLException, ClassNotFoundException {
        return batchDAO.generateNewId();
    }

    @Override
    public boolean deleteSelectedBatches(ArrayList<String> ids) throws SQLException {
        return batchDAO.deleteSelectedBatches(ids);
    }

    @Override
    public ArrayList<BatchDTO> getAvailableBatches() throws SQLException {
        ArrayList<Batch> batches = batchDAO.getAvailableBatches();
        ArrayList<BatchDTO> batchDTOS = new ArrayList<>();
        for (Batch batch : batches) {
            batchDTOS.add(new BatchDTO(batch.getId(), batch.getSDate(), batch.getDDate(), batch.getAvailableStatus(), batch.getTotalWeight(), batch.getCurrentWeight(), batch.getDeliveryAddress(), batch.getNoOfOrders(), batch.getShipmentType()));
        }
        return batchDTOS;
    }

    @Override
    public boolean updateBatchWeight(String batchId, int weight) throws SQLException {
        return batchDAO.updateBatchWeight(batchId, weight);
    }

    @Override
    public void setBatchStatus() throws SQLException {
        batchDAO.setStatus();
    }

    @Override
    public void setAvailableBatchStatus(int i, String batchId) throws SQLException {
        batchDAO.setAvailableStatus(i, batchId);
    }

    @Override
    public boolean setCurrentBatchOrders(String batchId, int i) throws SQLException {
        return batchDAO.setCurrentOrders(batchId, i);
    }
}
