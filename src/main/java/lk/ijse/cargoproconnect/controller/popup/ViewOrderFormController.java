package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.dto.CustomerDTO;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.OrderDTO;
import lk.ijse.cargoproconnect.dto.tm.OrderTM;
import lk.ijse.cargoproconnect.model.BatchModel;
import lk.ijse.cargoproconnect.model.CustomerModel;
import lk.ijse.cargoproconnect.model.DeliveryModel;
import lk.ijse.cargoproconnect.model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewOrderFormController implements Initializable {

    @FXML
    public Label lblBatchId;

    @FXML
    private Label lblOrderId;

    @FXML
    private JFXTextField txtOrderDate;

    @FXML
    private Label lblCustomerId;

    @FXML
    private JFXTextField txtFName;

    @FXML
    private JFXTextField txtLName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private Label lblIsChecked;

    @FXML
    private Label lblIsDeliver;

    @FXML
    private JFXTextField txtCheckBy;

    @FXML
    private JFXTextField txtCheckTime;

    @FXML
    private JFXTextField txtSDate;

    @FXML
    private JFXTextField txtDDate;

    @FXML
    private JFXTextField txtTo;

    @FXML
    private JFXTextField txtCustomerAddress;

    @FXML
    private JFXTextField txtWeight;

    private static OrderTM orderTM;

    public static void setOrderDetails(OrderTM orderTM) {
        ViewOrderFormController.orderTM = orderTM;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
    }

    private void setData() {
        try {
            BatchDTO batch = BatchModel.getBatch(orderTM.getBatchId());
            CustomerDTO customer = CustomerModel.getCustomer(orderTM.getCustomerId());
            OrderDTO order = OrderModel.getOrder(orderTM.getId());
            String deliverId = DeliveryModel.getDeliverId(order.getId());
            DeliveryDTO delivery = DeliveryModel.getDelivery(deliverId);
//                lblOrderId.setText(order.getId());
//                lblIsChecked.setText(order.getIsChecked() == 0 ? "UNCHECKED" : "CHECKED");
//                lblIsDeliver.setText(order.getIsDeliver() == 0 ? "NOT YET" : "DELIVER");
//                lblCustomerId.setText(orderTM.getCustomerId());
//                lblBatchId.setText(orderTM.getBatchId());
//                txtFName.setText(customer.getFName());
//                txtLName.setText(customer.getLName());
//                txtEmail.setText(customer.getEmail());
//                txtCustomerAddress.setText(delivery.getAddress());
//                txtSDate.setText(batch.getSDate());
//                txtDDate.setText(batch.getDDate());
//                txtTo.setText(batch.getDeliveryAddress());
//                txtWeight.setText(order.getWeight() + "Kg");
//                txtCheckBy.setText(orderTM.getCheckBy());
//                txtCheckTime.setText(orderTM.getCheckTime());
//                txtOrderDate.setText(order.getOrderDate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
