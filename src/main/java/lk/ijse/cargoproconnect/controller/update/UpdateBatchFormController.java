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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.bos.BatchBO;
import lk.ijse.cargoproconnect.bo.bos.impl.BatchBOImpl;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateBatchFormController implements Initializable {

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
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDiscard;

    static BatchDTO batch;
    private static ObservableList<String> cmbList = FXCollections.observableArrayList("SHIP", "FLIGHT");

    public static void setBatchDetails(BatchDTO batch) {
        UpdateBatchFormController.batch = batch;
    }

    //Dependency Injection (Property Injection)
    BatchBO batchBO = new BatchBOImpl();

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (cmbShipmentType.getValue() != null && txtTotalWeight.getFocusColor().equals(Color.web(Colors.GREEN)) && txtDeliverDate.getValue() != null && txtShipmentDate.getValue() != null && txtDeliverAddress.getFocusColor().equals(Color.web(Colors.GREEN))) {
            try {
                boolean isUpdated = batchBO.updateBatch(new BatchDTO(lblBatchId.getText(), txtShipmentDate.getValue(), txtDeliverDate.getValue(), txtTotalWeight.getText(), txtDeliverAddress.getText(), cmbShipmentType.getValue()));
                if (isUpdated) {
                    NotificationUtil.showNotification("Success", "Successfully " + lblBatchId.getText() + " Batch updated ", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
                }
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
                NotificationUtil.showNotification("Error", "OOPS! Something happen ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! Enter correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        loadShipmentTypes();
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setValid(txtTotalWeight, TextFieldValidator.getWeightPattern());
        TextFieldValidator.setValid(txtDeliverAddress, TextFieldValidator.getAddressPattern());

        TextFieldValidator.setFocus(txtTotalWeight, TextFieldValidator.getWeightPattern());
        TextFieldValidator.setFocus(txtDeliverAddress, TextFieldValidator.getAddressPattern());
    }

    private void setData() {
        lblBatchId.setText(batch.getId());
        cmbShipmentType.setValue(batch.getShipmentType());
        txtTotalWeight.setText(String.valueOf(batch.getTotalWeight()));
        txtShipmentDate.setValue(LocalDate.parse(batch.getSDate()));
        txtDeliverDate.setValue(LocalDate.parse(batch.getDDate()));
        txtDeliverAddress.setText(batch.getDeliveryAddress());
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
