package lk.ijse.cargoproconnect.bo.bos;

import javafx.collections.ObservableList;
import lk.ijse.cargoproconnect.bo.SuperBO;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.OrderDTO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderBO extends SuperBO {

    ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException;

    boolean addOrder(OrderDTO dto) throws SQLException, ClassNotFoundException;

    OrderDTO searchOrder(String id) throws SQLException, ClassNotFoundException;

    boolean updateOrder(OrderDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteOrder(String id) throws SQLException, ClassNotFoundException;

    String generateNewOrderId() throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getOrders(int i) throws SQLException;

    boolean orderIsChecked(String orderId) throws SQLException;

    boolean updateIsDeliver(String orderId) throws SQLException;

    boolean placeNewOrder(int totalPrice, int weight, DeliveryDTO delivery, ObservableList<OrderItemTM> observableList, OrderDTO order, double totalTax, double total, String paymentType) throws SQLException;
}
