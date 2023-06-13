package lk.ijse.cargoproconnect.bo.bos.impl;

import lk.ijse.cargoproconnect.bo.bos.PaymentBO;
import lk.ijse.cargoproconnect.dao.DAOFactory;
import lk.ijse.cargoproconnect.dao.daos.PaymentDAO;
import lk.ijse.cargoproconnect.dto.PaymentDTO;
import lk.ijse.cargoproconnect.entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentBOImpl implements PaymentBO {

    //Dependency Injection (Property Injection)
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public ArrayList<PaymentDTO> getAllPayments() throws SQLException, ClassNotFoundException {
        ArrayList<PaymentDTO> paymentDTOS = new ArrayList<>();
        ArrayList<Payment> payments = paymentDAO.getAll();
        for (Payment payment : payments) {
            paymentDTOS.add(new PaymentDTO(payment.getPaymentId(),payment.getPaymentType(),payment.getTotal(),payment.getTotalTax()));
        }
        return paymentDTOS;
    }

    @Override
    public boolean addPayment(PaymentDTO dto) throws SQLException, ClassNotFoundException {
        return paymentDAO.add(new Payment(dto.getId(),dto.getPaymentType(),dto.getTotal(),dto.getTotalTax()));
    }

    @Override
    public PaymentDTO searchPayment(String id) throws SQLException, ClassNotFoundException {
        Payment payment = paymentDAO.search(id);
        return new PaymentDTO(payment.getPaymentId(),payment.getPaymentType(),payment.getTotal(),payment.getTotalTax());
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) throws SQLException, ClassNotFoundException {
        return paymentDAO.update(new Payment(dto.getId(),dto.getPaymentType(),dto.getTotal(),dto.getTotalTax()));
    }

    @Override
    public boolean deletePayment(String id) throws SQLException, ClassNotFoundException {
        return paymentDAO.delete(id);
    }

    @Override
    public String generateNewPaymentId() throws SQLException, ClassNotFoundException {
        return paymentDAO.generateNewId();
    }
}
