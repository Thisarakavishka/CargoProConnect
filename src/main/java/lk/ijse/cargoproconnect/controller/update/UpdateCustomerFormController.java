package lk.ijse.cargoproconnect.controller.update;

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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.dto.CustomerDTO;
import lk.ijse.cargoproconnect.model.CustomerModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomerFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    public JFXButton btnBack;

    @FXML
    private Label lblCustomerId;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtContactNumber1;

    @FXML
    private JFXTextField txtContactNumber2;

    @FXML
    private JFXComboBox<String> cmbDocumentType;

    @FXML
    private JFXTextField txtDocumentNumber;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDiscard;

    static CustomerDTO customer;

    private static ObservableList<String> cmbList = FXCollections.observableArrayList("PASSPORT", "DRIVING LICENCE", "NATIONAL IDENTITY CARD");

    public static void setCustomerDetails(CustomerDTO customer) {
        UpdateCustomerFormController.customer = customer;
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CustomerForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtFirstName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtLastName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtContactNumber1.getFocusColor().equals(Color.web(Colors.GREEN)) && txtContactNumber2.getFocusColor().equals(Color.web(Colors.GREEN)) && txtEmail.getFocusColor().equals(Color.web(Colors.GREEN)) && cmbDocumentType.getValue() != null && txtDocumentNumber.getFocusColor().equals(Color.web(Colors.GREEN)) && txtEmail.getFocusColor().equals(Color.web(Colors.GREEN))) {
            try {
                boolean isUpdated = CustomerModel.updateCustomer(new CustomerDTO(lblCustomerId.getText(), txtFirstName.getText(), txtLastName.getText(), txtContactNumber1.getText(), txtContactNumber2.getText(), cmbDocumentType.getValue(), txtDocumentNumber.getText(), txtEmail.getText()));
                if (isUpdated) {
                    NotificationUtil.showNotification("Success", "Successfully " + lblCustomerId.getText() + " Customer updated ", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CustomerForm.fxml")));
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                NotificationUtil.showNotification("OOPS!", "Something happen", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! Enter correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        validateTextFields();
        loadDocumentTypes();
    }

    void loadDocumentTypes() {
        cmbDocumentType.setItems(cmbList);
    }

    private void validateTextFields() {
        TextFieldValidator.setFocus(txtEmail, TextFieldValidator.getEmailPattern());
        TextFieldValidator.setFocus(txtContactNumber1, TextFieldValidator.getContactNumberPattern());
        TextFieldValidator.setFocus(txtContactNumber2, TextFieldValidator.getContactNumberPattern());
        TextFieldValidator.setFocus(txtFirstName, TextFieldValidator.getNamePattern());
        TextFieldValidator.setFocus(txtLastName, TextFieldValidator.getNamePattern());
        txtDocumentNumber.setOnKeyTyped(keyEvent -> {
            if (cmbDocumentType.getValue() == null) {
            } else if (cmbDocumentType.getValue().equalsIgnoreCase("national identity card")) {
                TextFieldValidator.setFocus(txtDocumentNumber, TextFieldValidator.getOldIDPattern());
            } else if (cmbDocumentType.getValue().equalsIgnoreCase("passport")) {
                TextFieldValidator.setFocus(txtDocumentNumber, TextFieldValidator.getPassportNumber());
            } else if (cmbDocumentType.getValue().equalsIgnoreCase("driving licence")) {
                TextFieldValidator.setFocus(txtDocumentNumber, TextFieldValidator.getDrivingNumber());
            }
        });
        TextFieldValidator.setValid(txtEmail, TextFieldValidator.getEmailPattern());
        TextFieldValidator.setValid(txtContactNumber1, TextFieldValidator.getContactNumberPattern());
        TextFieldValidator.setValid(txtContactNumber2, TextFieldValidator.getContactNumberPattern());
        TextFieldValidator.setValid(txtFirstName, TextFieldValidator.getNamePattern());
        TextFieldValidator.setValid(txtLastName, TextFieldValidator.getNamePattern());
        if (cmbDocumentType.getValue() == null) {
        } else if (cmbDocumentType.getValue().equalsIgnoreCase("national identity card")) {
            TextFieldValidator.setValid(txtDocumentNumber, TextFieldValidator.getOldIDPattern());
        } else if (cmbDocumentType.getValue().equalsIgnoreCase("passport")) {
            TextFieldValidator.setValid(txtDocumentNumber, TextFieldValidator.getPassportNumber());
        } else if (cmbDocumentType.getValue().equalsIgnoreCase("driving licence")) {
            TextFieldValidator.setValid(txtDocumentNumber, TextFieldValidator.getDrivingNumber());
        }
    }

    private void setData() {
        lblCustomerId.setText(customer.getId());
        txtFirstName.setText(customer.getFName());
        txtLastName.setText(customer.getLName());
        txtContactNumber1.setText(customer.getContactN1());
        txtContactNumber2.setText(customer.getContactN2());
        cmbDocumentType.setValue(customer.getDocumentType());
        txtDocumentNumber.setText(customer.getDocumentNumber());
        txtEmail.setText(customer.getEmail());
    }

    @FXML
    public void btnBackOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CustomerForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
