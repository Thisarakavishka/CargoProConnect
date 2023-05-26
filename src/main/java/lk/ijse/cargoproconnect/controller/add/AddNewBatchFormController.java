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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.dto.Batch;
import lk.ijse.cargoproconnect.model.BatchModel;
import lk.ijse.cargoproconnect.model.LoginModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AddNewBatchFormController implements Initializable {

    @FXML
    public JFXButton btnBack;

    @FXML
    private AnchorPane rootChange;

    @FXML
    private Label lblBatchId;

    @FXML
    private JFXTextField txtTotalWeight;

    @FXML
    private JFXTextField txtDeliverAddress;

    @FXML
    private JFXComboBox<String> cmbShipmentType;

    @FXML
    private DatePicker txtShipmentDate;

    @FXML
    private DatePicker txtDeliverDate;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDiscard;

    private static ObservableList<String> cmbList = FXCollections.observableArrayList("SHIP", "FLIGHT");

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if(cmbShipmentType.getValue() != null && txtTotalWeight.getFocusColor().equals(Color.web(Colors.GREEN)) && txtDeliverDate.getValue() != null && txtShipmentDate.getValue() != null && txtDeliverAddress.getFocusColor().equals(Color.web(Colors.GREEN))){
            try {
                boolean isAdded = BatchModel.addNewBatch(new Batch(lblBatchId.getText(), txtShipmentDate.getValue(), txtDeliverDate.getValue(), txtTotalWeight.getText(), txtDeliverAddress.getText(), cmbShipmentType.getValue()));
                if (isAdded) {
                    NotificationUtil.showNotification("Success", "Successfully " + lblBatchId.getText() + " Batch added " + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
                }
            } catch (SQLException | IOException e) {
                NotificationUtil.showNotification("Error", "OOPS!  Something happen" + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
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
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextBatchId();
        loadShipmentTypes();
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setFocus(txtTotalWeight, TextFieldValidator.getWeightPattern());
        TextFieldValidator.setFocus(txtDeliverAddress, TextFieldValidator.getAddressPattern());
    }

    void generateNextBatchId() {
        try {
            lblBatchId.setText(BatchModel.getNextTaxId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void loadShipmentTypes() {
        cmbShipmentType.setItems(cmbList);
    }

    @FXML
    public void btnBackOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
