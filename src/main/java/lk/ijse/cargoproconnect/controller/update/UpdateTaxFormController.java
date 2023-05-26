package lk.ijse.cargoproconnect.controller.update;

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
import lk.ijse.cargoproconnect.dto.Tax;
import lk.ijse.cargoproconnect.model.TaxModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateTaxFormController implements Initializable {

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
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDiscard;

    static Tax tax;

    public static void setTaxDetails(Tax tax) {
        UpdateTaxFormController.tax = tax;
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

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtPercentage.getFocusColor().equals(Color.web(Colors.GREEN)) && txtDescription.getFocusColor().equals(Color.web(Colors.GREEN))) {
            try {
                boolean isUpdated = TaxModel.updateTax(new Tax(taxId.getText(), txtName.getText(), Double.parseDouble(txtPercentage.getText()), txtDescription.getText()));
                if (isUpdated) {
                    NotificationUtil.showNotification("Success", "Successfully " + txtName.getText() + " Tax updated ", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));

                }
            } catch (SQLException | IOException e) {
                NotificationUtil.showNotification("OOPS!", "Something happen ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("OOPS!", "OOPS! Enter values ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setValid(txtName, TextFieldValidator.getNamePattern());
        TextFieldValidator.setValid(txtDescription, TextFieldValidator.getNamePattern());
        TextFieldValidator.setValid(txtPercentage, TextFieldValidator.getWeightPattern());

        TextFieldValidator.setFocus(txtName, TextFieldValidator.getNamePattern());
        TextFieldValidator.setFocus(txtDescription, TextFieldValidator.getNamePattern());
        TextFieldValidator.setFocus(txtPercentage, TextFieldValidator.getWeightPattern());
    }

    private void setData() {
        taxId.setText(tax.getId());
        txtName.setText(tax.getName());
        txtPercentage.setText(String.valueOf(tax.getPercentage()));
        txtDescription.setText(tax.getDescription());
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
