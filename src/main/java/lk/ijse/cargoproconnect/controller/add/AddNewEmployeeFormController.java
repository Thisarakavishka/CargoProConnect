package lk.ijse.cargoproconnect.controller.add;

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
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.EmployeeBO;
import lk.ijse.cargoproconnect.dto.EmployeeDTO;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.SecurityUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewEmployeeFormController implements Initializable {

    @FXML
    public JFXButton btnBack;

    @FXML
    private AnchorPane rootChange;

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
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDiscard;

    private static ObservableList<String> cmbList = FXCollections.observableArrayList("PASSPORT", "DRIVING LICENCE", "NATIONAL IDENTITY CARD");

    //Dependency Injection (Property Injection)
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.EMPLOYEE);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if(txtUserName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtPassword.getFocusColor().equals(Color.web(Colors.GREEN)) && txtEmail.getFocusColor().equals(Color.web(Colors.GREEN)) && cmbDocumentType.getValue() != null && txtDocumentNumber.getFocusColor().equals(Color.web(Colors.GREEN))){
            try {
                boolean isAdded = employeeBO.addEmployee(new EmployeeDTO(lblEmployeeId.getText(), SecurityUtil.encoder(txtUserName.getText()), SecurityUtil.encoder(txtPassword.getText()), txtEmail.getText(), cmbDocumentType.getValue(), txtDocumentNumber.getText(), 1));
                if (isAdded) {
                    NotificationUtil.showNotification("Success", "New Employee " + txtUserName.getText() + " added successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeForm.fxml")));
                }
            } catch (SQLException | IOException | ClassNotFoundException e) {
                NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                e.printStackTrace();
            }
        }else{
            NotificationUtil.showNotification("Error", "OOPS! Enter correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextEmployeeId();
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
    }

    void generateNextEmployeeId() {
        try {
            lblEmployeeId.setText(employeeBO.generateNewEmployeeId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
