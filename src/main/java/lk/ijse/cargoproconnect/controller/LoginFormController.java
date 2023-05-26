package lk.ijse.cargoproconnect.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.model.LoginModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.SecurityUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    @FXML
    public JFXTextField txtLoginUserName;

    @FXML
    public JFXPasswordField txtLoginPassword;

    @FXML
    public ImageView imageAfter;

    @FXML
    public Pane paneImage;

    @FXML
    public JFXButton btnLogin;

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView image;

    @FXML
    private JFXButton btnMainLogin;

    @FXML
    private Pane paneLogin;

    @FXML
    private Label lblLoginPassword;

    @FXML
    void btnBackOnAction(ActionEvent event) {
        paneImage.setVisible(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), image);
        translateTransition.setToY(0);
        translateTransition.play();
        paneLogin.setVisible(false);
        translateTransition.setOnFinished(event1 -> {
            btnMainLogin.setVisible(true);
        });
        txtLoginPassword.setText(null);
        txtLoginUserName.setText(null);
        lblLoginPassword.setText(null);

    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        if (txtLoginUserName.getFocusColor().equals(Color.web(Colors.GREEN)) && !txtLoginPassword.getText().isEmpty() && !txtLoginPassword.getText().isBlank()) {
            Stage stage = (Stage) root.getScene().getWindow();
            try {
                boolean isAdmin = LoginModel.searchAdmin(txtLoginUserName.getText(), txtLoginPassword.getText());
                boolean isEmployee = LoginModel.searchEmployee(SecurityUtil.encoder(txtLoginUserName.getText()), SecurityUtil.encoder(txtLoginPassword.getText()));

                if (isAdmin | isEmployee) {
                    lblLoginPassword.setText(null);
                    if (isAdmin) {
                        lblLoginPassword.setText(null);
                        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/AdminDashBoardForm.fxml"))));
                        stage.setTitle("Admin Form");
                        NotificationUtil.showNotification("Successfully Login", "Welcome to Cargo Pro Connect " + LoginModel.getAdminUserName(), NotificationUtil.NotificationType.NOTIFICATION, Duration.seconds(5));
                    } else if (isEmployee) {
                        lblLoginPassword.setText(null);
                        boolean isActive = LoginModel.searchStatus(SecurityUtil.encoder(txtLoginUserName.getText()), SecurityUtil.encoder(txtLoginPassword.getText()));
                        if (isActive) {
                            lblLoginPassword.setText(null);
                            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeDashBoardForm.fxml"))));
                            stage.setTitle("Employee Form");
                            NotificationUtil.showNotification("Successfully Login", "Welcome to Cargo Pro Connect " + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.NOTIFICATION, Duration.seconds(5));
                        } else {
                            lblLoginPassword.setText("Your Current status is Inactive please contact admin");
                            lblLoginPassword.setStyle("-fx-text-fill: " + Colors.RED + ";");
                        }
                    }
                } else {
                    lblLoginPassword.setText("Incorrect password or username");
                    lblLoginPassword.setStyle("-fx-text-fill: " + Colors.RED + ";");
                }
            } catch (SQLException |
                    IOException e) {
                e.printStackTrace();
            }
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        }else{
            NotificationUtil.showNotification("OOPS! ", "Enter correct values !", NotificationUtil.NotificationType.INFORMATION, Duration.seconds(5));
        }
    }

    @FXML
    void btnMainLoginOnAction(ActionEvent event) {
        btnMainLogin.setVisible(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), image);
        translateTransition.setToY(-1000);
        translateTransition.play();
        translateTransition.setOnFinished(event1 -> {
            paneLogin.setVisible(true);
            paneImage.setVisible(true);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hidePanes();
        setEnterAction();
        setValidation();
    }

    private void setValidation() {
        TextFieldValidator.setFocus(txtLoginUserName, TextFieldValidator.getUserNamePattern());
    }

    private void setEnterAction() {
        txtLoginUserName.setOnAction(event -> {
            txtLoginPassword.requestFocus();
        });
        txtLoginPassword.setOnAction(event -> {
            btnLogin.fire();
        });
    }

    private void hidePanes() {
        paneLogin.setVisible(false);
        paneImage.setVisible(false);
        txtLoginPassword.setText(null);
        txtLoginUserName.setText(null);
    }
}
