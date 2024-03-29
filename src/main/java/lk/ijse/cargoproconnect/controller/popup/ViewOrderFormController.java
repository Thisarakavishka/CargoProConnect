package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.*;
import lk.ijse.cargoproconnect.bo.bos.impl.BatchBOImpl;
import lk.ijse.cargoproconnect.dto.*;
import lk.ijse.cargoproconnect.dto.tm.OrderTM;

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

    //Dependency Injection (Property Injection)
    BatchBO batchBO = new BatchBOImpl();
    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    DeliverDetailBO detailBO = (DeliverDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DELIVER_DETAIL);
    OrderDeliverDetailBO orderDeliverDetailBO = (OrderDeliverDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER_DELIVER_DETAIL);
    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
    }

    private void setData() {
        try {
            BatchDTO batch = batchBO.searchBatch(orderTM.getBatchId());
            CustomerDTO customer = customerBO.searchCustomer(orderTM.getCustomerId());
            OrderDTO order = orderBO.searchOrder(orderTM.getId());
            OrderDeliverDetailsDTO searchDto = orderDeliverDetailBO.search(order.getId());
            String deliverId = searchDto.getDeliverId();
            DeliveryDTO delivery = detailBO.searchDeliverDetail(deliverId);
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
