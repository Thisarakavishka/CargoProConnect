package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.MailUtil;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import javax.mail.MessagingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MailFormController implements Initializable {

    @FXML
    public AnchorPane root;

    @FXML
    private JFXTextField txtTo;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    private JFXTextArea txtContent;

    @FXML
    private JFXButton btnSend;

    @FXML
    private JFXButton btnDiscard;

    @FXML
    private JFXButton btnExit;


    private static List<String> emails = new ArrayList<>();
    private static String email;

    public static void setMails(List<String> emails) {
        MailFormController.emails = emails;
    }

    public static void setMail(String email) {
        MailFormController.email = email;
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        txtTo.setText(null);
        txtContent.setText(null);
        txtSubject.setText(null);
        stage.close();
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        if(txtTo.getFocusColor().equals(Color.web(Colors.GREEN)) && txtSubject.getText() != null && !txtSubject.getText().isBlank() && txtContent.getText() != null && !txtContent.getText().isBlank()){
            try {
                MailUtil.sendMail(txtTo.getText(), txtSubject.getText(), txtContent.getText());
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            } catch (MessagingException e) {
                NotificationUtil.showNotification("OOPS!", "OOPS! something happen", NotificationUtil.NotificationType.INFORMATION, Duration.seconds(5));
            }
            txtTo.setText(null);
            txtContent.setText(null);
            txtSubject.setText(null);
        }else{
            NotificationUtil.showNotification("OOPS!", "OOPS! Enter values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValidation();
        setEmail();
    }

    private void setValidation() {
        TextFieldValidator.setFocus(txtTo, TextFieldValidator.getEmailPattern());
    }

    private void setEmail() {
        if (email == null && emails.isEmpty()) {
            txtTo.setText("");
        } else if (email != null) {
            txtTo.setText(email);
            txtTo.setFocusColor(Paint.valueOf(Colors.GREEN));
            email = null;
        } else {
            StringBuilder textBuilder = new StringBuilder();
            for (String email : emails) {
                textBuilder.append(email).append(", ");
            }
            String text = textBuilder.toString();
            text = text.substring(0, text.length() - 2);
            txtTo.setText(text);
            txtTo.setFocusColor(Paint.valueOf(Colors.GREEN));
            emails.clear();
        }
    }
}
