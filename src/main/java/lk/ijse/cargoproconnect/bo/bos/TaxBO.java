package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.TaxDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TaxBO extends SuperBO {

    ArrayList<TaxDTO> getAllTaxes() throws SQLException, ClassNotFoundException;

    boolean addTax(TaxDTO dto) throws SQLException, ClassNotFoundException;

    TaxDTO searchTax(String id) throws SQLException, ClassNotFoundException;

    boolean updateTax(TaxDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteTax(String id) throws SQLException, ClassNotFoundException;

    String generateNewTaxId() throws SQLException, ClassNotFoundException;

    ArrayList<TaxDTO> getAllTaxes(ArrayList<String> ids) throws SQLException;

    boolean deleteSelectedTaxes(ArrayList<String> ids) throws SQLException;
}
