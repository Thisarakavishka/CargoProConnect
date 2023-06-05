package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.BatchBO;
import lk.ijse.cargoproconnect.dao.daos.BatchDAO;
import lk.ijse.cargoproconnect.dao.daos.impl.BatchDAOImpl;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.entity.Batch;

import java.sql.SQLException;
import java.util.ArrayList;

public class BatchBOImpl implements BatchBO {

    //Dependency Injection (Property Injection)
    BatchDAO batchDAO = new BatchDAOImpl();

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
    public boolean existBatch(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewBatchId() throws SQLException, ClassNotFoundException {
        return batchDAO.generateNewId();
    }
}
