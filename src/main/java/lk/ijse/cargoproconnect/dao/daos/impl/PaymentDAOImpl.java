package lk.ijse.cargoproconnect.dao.daos.impl;

import lk.ijse.cargoproconnect.dao.daos.PaymentDAO;
import lk.ijse.cargoproconnect.entity.Payment;
import lk.ijse.cargoproconnect.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public ArrayList<Payment> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean add(Payment dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO payment(payment_id, payment_type, total, total_tax) VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql, dto.getPaymentId(), dto.getPaymentType(), dto.getTotal(),dto.getTotalTax());
    }

    @Override
    public Payment search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean update(Payment dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature is not implemented yet ");
    }

    @Override
    public String generateNewId() throws SQLException {
        String sql = "SELECT MAX(payment_id) FROM payment";
        ResultSet resultSet = CrudUtil.execute(sql);

        String currentMaxId = null;
        if (resultSet.next()) {
            currentMaxId = resultSet.getString(1);
        }
        if (currentMaxId == null) {
            return "P0001";
        }
        return "P" + String.format("%04d", Integer.parseInt(currentMaxId.substring(1)) + 1);
    }

}
