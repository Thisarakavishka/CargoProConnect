package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.OrderBO;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.OrderDTO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;
import lk.ijse.cargoproconnect.model.PaymentModel;
import lk.ijse.cargoproconnect.model.PlaceOrderModel;
import lk.ijse.cargoproconnect.util.MailUtil;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.ReceiptGeneratorUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class PaymentFormController implements Initializable {

    @FXML
    public AnchorPane root;

    @FXML
    private Label lblPaymentId;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtTotalTaxAmount;

    @FXML
    private JFXTextField txtTotalWeight;

    @FXML
    private JFXTextField txtTotalAmount;

    @FXML
    private JFXComboBox<String> cmbPaymentMethod;

    @FXML
    private JFXButton btnPay;

    @FXML
    private JFXButton btnDiscard;

    private static AnchorPane parentRoot;
    public static int price = 10;
    private static int weight;
    private static DeliveryDTO delivery;
    private static ObservableList<OrderItemTM> observableList;
    private static String orderId;
    private static String customerId;
    private static String batchId;
    private static String orderDate;
    private static String email;
    private static double totalTax;
    private static final ObservableList<String> cmbList = FXCollections.observableArrayList("CASH", "CARD");
    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    //Dependency Injection (Property Injection)
    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    public static void setPlaceOrderDetails(int weight, DeliveryDTO delivery, ObservableList<OrderItemTM> observableList, String orderId, String customerId, String batchId, String orderDate, double totalTax) {
        PaymentFormController.weight = weight;
        PaymentFormController.delivery = delivery;
        PaymentFormController.observableList = observableList;
        PaymentFormController.orderId = orderId;
        PaymentFormController.customerId = customerId;
        PaymentFormController.batchId = batchId;
        PaymentFormController.orderDate = orderDate;
        PaymentFormController.totalTax = totalTax;
    }

    public static void setParentRoot(AnchorPane rootChange) {
        PaymentFormController.parentRoot = rootChange;
    }

    public static void setCustomerEmail(String email) {
        PaymentFormController.email = email;
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnPayOnAction(ActionEvent event) {
        if (cmbPaymentMethod.getValue() != null) {
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                String totalPrice = df.format(price * weight);
                int total = price * weight;
//                boolean isAdded = PlaceOrderModel.placeNewOrder(total, weight, delivery, observableList, new OrderDTO(orderId, customerId, lblPaymentId.getText(), batchId, orderDate), totalTax, Double.parseDouble(totalPrice), cmbPaymentMethod.getValue());
                boolean isAdded = orderBO.placeNewOrder(total, weight, delivery, observableList, new OrderDTO(orderId, customerId, lblPaymentId.getText(), batchId, orderDate), totalTax, Double.parseDouble(totalPrice), cmbPaymentMethod.getValue());
                if (isAdded) {
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.close();
                    try {
                        MailUtil.sendMail(email, "Order receipt", ReceiptGeneratorUtil.generate(customerId, batchId, weight, price * weight, delivery, cmbPaymentMethod.getValue()));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    NotificationUtil.showNotification("Success", "SuccessFully  complete order", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    parentRoot.getChildren().clear();
                    parentRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/OrderForm.fxml")));
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! select payment method", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextPaymentId();
        setData();
    }

    private void setData() {
        txtPrice.setText(price + " $");
        txtTotalWeight.setText(String.valueOf(weight));
        txtTotalTaxAmount.setText(decimalFormatter.format(totalTax) + " $");
        txtTotalAmount.setText(decimalFormatter.format((price * weight) + totalTax) + " $");
        cmbPaymentMethod.setItems(cmbList);
    }

    void generateNextPaymentId() {
        try {
            lblPaymentId.setText(PaymentModel.getNextPaymentId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
