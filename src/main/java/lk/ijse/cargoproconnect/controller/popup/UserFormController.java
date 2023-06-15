package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.EmployeeBO;
import lk.ijse.cargoproconnect.dto.EmployeeDTO;
import lk.ijse.cargoproconnect.model.EmployeeModel;
import lk.ijse.cargoproconnect.model.LoginModel;
import lk.ijse.cargoproconnect.util.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {

    @FXML
    public AnchorPane root;

    @FXML
    public JFXTextField txtNewPassword;

    @FXML
    public JFXTextField txtNewUserName;

    @FXML
    public JFXTextField txtNewEmail;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXButton btnGetCode;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXTextField txtVerificationCode;

    @FXML
    private JFXButton btnDiscard;

    private static final String email = LoginModel.getEmployeeEmail();
    private static final String userName = LoginModel.getEmployeeUserName();
    private String otp;

    //Dependency Injection (Property Injection)
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.EMPLOYEE);

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnGetCodeOnAction(ActionEvent event) {
        if (txtNewEmail.getFocusColor().equals(Color.web(Colors.GREEN)) && txtNewUserName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtNewPassword.getFocusColor().equals(Color.web(Colors.GREEN))) {
            NotificationUtil.showNotification("Success", "Check OTP in your mail box", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
            otp = MailUtil.sendOTP(email);
        } else {
            NotificationUtil.showNotification("Error", "Input correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (txtVerificationCode.getText().equalsIgnoreCase(otp)) {
            try {
                boolean isUpdate = employeeBO.updateUser(new EmployeeDTO(LoginModel.getEmployeeId(), SecurityUtil.encoder(txtNewUserName.getText()), SecurityUtil.encoder(txtNewPassword.getText()), txtNewEmail.getText()));
                if (isUpdate) {
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.close();
                    NotificationUtil.showNotification("Success", "Successfully update your information", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    NotificationUtil.showNotification("Information", "Can you again login to system for better experience", NotificationUtil.NotificationType.INFORMATION, Duration.seconds(5));
                } else {
                    NotificationUtil.showNotification("OOPS!", "In this time unable to update please try again later", NotificationUtil.NotificationType.INFORMATION, Duration.seconds(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                NotificationUtil.showNotification("OOPS!", "OOPS! something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("OOPS!", "OOPS! Enter correct OTP", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        setValidation();
    }

    private void setValidation() {
        TextFieldValidator.setFocus(txtNewEmail, TextFieldValidator.getEmailPattern());
        TextFieldValidator.setFocus(txtNewPassword, TextFieldValidator.getPasswordPattern());
        TextFieldValidator.setFocus(txtNewUserName, TextFieldValidator.getUserNamePattern());
    }

    private void setData() {
        txtEmail.setText(email);
        txtUserName.setText(userName);
    }
}
