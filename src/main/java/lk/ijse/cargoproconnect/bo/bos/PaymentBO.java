package lk.ijse.cargoproconnect.bo.bos;

import lk.ijse.cargoproconnect.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentBO {

    ArrayList<PaymentDTO> getAllPayments() throws SQLException, ClassNotFoundException;

    boolean addPayment(PaymentDTO dto) throws SQLException, ClassNotFoundException;

    PaymentDTO searchPayment(String id) throws SQLException, ClassNotFoundException;

    boolean updatePayment(PaymentDTO dto) throws SQLException, ClassNotFoundException;

    boolean deletePayment(String id) throws SQLException, ClassNotFoundException;

    String generateNewPaymentId() throws SQLException, ClassNotFoundException;
}
