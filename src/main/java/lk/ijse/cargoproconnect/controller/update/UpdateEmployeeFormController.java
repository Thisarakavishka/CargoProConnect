package lk.ijse.cargoproconnect.controller.update;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
import lk.ijse.cargoproconnect.dto.Employee;
import lk.ijse.cargoproconnect.model.EmployeeModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.SecurityUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateEmployeeFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    public JFXCheckBox checkBoxBlock;

    @FXML
    public JFXButton btnBack;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox<String> cmbDocumentType;

    @FXML
    private JFXTextField txtDocumentNumber;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDiscard;

    static Employee employee;
    private static ObservableList<String> cmbList = FXCollections.observableArrayList("PASSPORT", "DRIVING LICENCE", "NATIONAL IDENTITY CARD");

    public static void setEmployeeDetails(Employee employee) {
        UpdateEmployeeFormController.employee = employee;
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtUserName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtPassword.getFocusColor().equals(Color.web(Colors.GREEN)) && txtEmail.getFocusColor().equals(Color.web(Colors.GREEN)) && cmbDocumentType.getValue() != null && txtDocumentNumber.getFocusColor().equals(Color.web(Colors.GREEN))) {
            try {
                int status = 1;
                if (checkBoxBlock.isSelected()) {
                    status = 0;
                }
                boolean isUpdated = EmployeeModel.updateEmployee(new Employee(lblEmployeeId.getText(), SecurityUtil.encoder(txtUserName.getText()), SecurityUtil.encoder(txtPassword.getText()), txtEmail.getText(), cmbDocumentType.getValue(), txtDocumentNumber.getText(), status));
                if (isUpdated) {
                    NotificationUtil.showNotification("Success", "Employee " + txtUserName.getText() + " Update successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeForm.fxml")));

                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! Enter correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        loadDocumentTypes();
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setFocus(txtEmail, TextFieldValidator.getEmailPattern());
        TextFieldValidator.setFocus(txtUserName, TextFieldValidator.getUserNamePattern());
        TextFieldValidator.setFocus(txtPassword, TextFieldValidator.getPasswordPattern());
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
        TextFieldValidator.setValid(txtUserName, TextFieldValidator.getUserNamePattern());
        TextFieldValidator.setValid(txtPassword, TextFieldValidator.getPasswordPattern());
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
        lblEmployeeId.setText(employee.getId());
        txtUserName.setText(SecurityUtil.decoder(employee.getUsername()));
        txtPassword.setText(SecurityUtil.decoder(employee.getPassword()));
        txtEmail.setText(employee.getEmail());
        txtDocumentNumber.setText(employee.getDocumentNumber());
        cmbDocumentType.setValue(employee.getDocumentType());
    }

    void loadDocumentTypes() {
        cmbDocumentType.setItems(cmbList);
    }

    @FXML
    public void btnBackOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
