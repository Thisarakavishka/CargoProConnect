package lk.ijse.cargoproconnect.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.model.LoginModel;
import lk.ijse.cargoproconnect.util.NotificationUtil;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class EmployeeDashBoardFormController implements Initializable {

    public JFXButton btnMail;
    public JFXButton btnSetting;
    @FXML
    private Label lblDate;

    @FXML
    private JFXButton btnUser;

    @FXML
    private Label lblUser;

    @FXML
    private AnchorPane rootChange;

    @FXML
    private JFXButton btnDashBoard;

    @FXML
    private JFXButton btnOrder;

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private JFXButton btnBatch;

    @FXML
    private JFXButton btnTax;

    @FXML
    private JFXButton btnCategory;

    @FXML
    private JFXButton btnReport;

    @FXML
    private JFXButton btnLogout;

    @FXML
    void btnBatchOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCategoryOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CustomerForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDashBoardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeDashBoardBodyForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        Stage stage = (Stage) rootChange.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/LoginForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        NotificationUtil.showNotification("Successfully Logout", "Successfully Logout from Cargo Pro Connect " + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.NOTIFICATION, Duration.seconds(5));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/OrderForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/ReportForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnTaxOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUserOnAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/UserForm.fxml"))));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setY(135);
            stage.setX(650);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnMailOnAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/MailForm.fxml"))));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setY(250);
            stage.setX(1220);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashBord();
        setDate();
        setUser();
    }

    private void setUser() {
        lblUser.setText(LoginModel.getEmployeeUserName());
    }

    void setDate() {
        lblDate.setText(new SimpleDateFormat("yyyy/MM/dd \n HH:mm:ss").format(Calendar.getInstance().getTime()));
    }

    private void loadDashBord() {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeDashBoardBodyForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
