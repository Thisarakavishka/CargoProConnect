package lk.ijse.cargoproconnect.controller.add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.model.LoginModel;
import lk.ijse.cargoproconnect.model.TaxModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewTaxFormController implements Initializable {

    @FXML
    public JFXButton btnBack;

    @FXML
    private AnchorPane rootChange;

    @FXML
    private JFXTextField txtName;

    @FXML
    private Label taxId;

    @FXML
    private JFXTextField txtPercentage;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDiscard;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (txtName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtPercentage.getFocusColor().equals(Color.web(Colors.GREEN)) && txtDescription.getFocusColor().equals(Color.web(Colors.GREEN))) {
            try {
                boolean isAdded = TaxModel.addNewTax(new TaxDTO(taxId.getText(), txtName.getText(), Double.parseDouble(txtPercentage.getText()), txtDescription.getText()));
                if (isAdded) {
                    NotificationUtil.showNotification("Success", "Successfully " + txtName.getText() + " Tax added ", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));
                }
            } catch (SQLException | IOException e) {
                NotificationUtil.showNotification("Error", "OOPS! Something happen" + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                e.printStackTrace();
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! Input values" + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextTaxId();
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setFocus(txtName, TextFieldValidator.getNamePattern());
        TextFieldValidator.setFocus(txtDescription, TextFieldValidator.getNamePattern());
        TextFieldValidator.setFocus(txtPercentage, TextFieldValidator.getWeightPattern());
    }

    void generateNextTaxId() {
        try {
            taxId.setText(TaxModel.getNextTaxId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnBackOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
